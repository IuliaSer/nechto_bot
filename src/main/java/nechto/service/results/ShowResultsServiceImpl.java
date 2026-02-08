package nechto.service.results;

import lombok.RequiredArgsConstructor;
import nechto.cache.ScoresStateCache;
import nechto.dto.AggregateScoresDto;
import nechto.dto.ScoresDto;
import nechto.exception.EntityNotFoundException;
import nechto.repository.ScoresRepository;
import nechto.service.GameService;
import nechto.service.ScoresService;
import nechto.service.UserService;
import nechto.service.results.DateParsers;
import nechto.service.results.ScoresDtoMapper;
import nechto.service.results.ShowResultsService;
import nechto.service.results.TableRenderer;
import nechto.service.results.TimeRange;
import nechto.utils.NumFmt;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static nechto.enums.Authority.ROLE_USER;
import static nechto.utils.BotUtils.getSendMessageWithMarkDown;

@Service
@RequiredArgsConstructor
public class ShowResultsServiceImpl implements ShowResultsService {
    private final ScoresRepository scoresRepository;
    private final ScoresService scoresService;
    private final GameService gameService;
    private final ScoresStateCache scoresStateCache;
    private final UserService userService;
    private final ScoresDtoMapper scoresDtoMapper;

    @Override
    public BotApiMethod<?> showResultsForADay(String ymd, long userId) {
        var range = TimeRange.forDay(DateParsers.parseYmd(ymd));
        var body = renderDayMonth(range);
        return getSendMessageWithMarkDown(userId, body);
    }

    @Override
    public BotApiMethod<?> showResultsForAMonth(String ym, long userId) {
        var range = TimeRange.forMonth(DateParsers.parseYm(ym));
        var body = renderDayMonth(range);
        return getSendMessageWithMarkDown(userId, body);
    }

    @Override
    public BotApiMethod<?> showResultsForAQuarter(long userId) {
        var now = LocalDateTime.now();
        var range = TimeRange.forQuarter(now);
        var body = renderQuarter(range);
        return getSendMessageWithMarkDown(userId, body);
    }

    @Override
    public BotApiMethod<?> showResultsForAGame(long userId) {
        long gameId = resolveGameId(userId);
        var scores = scoresRepository.findAllByGameId(gameId);
        var dtos = scoresDtoMapper.mapGameScores(scores);
        var body = renderGame(dtos);
        return getSendMessageWithMarkDown(userId, body);
    }

    private String renderDayMonth(TimeRange range) {
        var aggs = scoresRepository.aggregateByUserBetween(range.start(), range.end());

        var t = new TableRenderer()
                .addColumn("Ник", 4)
                .addColumn(" Очки", 5);
        aggs.stream()
                .sorted(Comparator.comparing(AggregateScoresDto::getScores).reversed())
                .forEach(a -> t.addRow(a.getUsername(), NumFmt.withSign(a.getScores(), NumFmt.two(a.getScores()))));
        return t.renderAsInlineCodeLines();
    }

    private String renderQuarter(TimeRange range) {
        var aggs = scoresRepository.aggregateByUserBetween(range.start(), range.end());

        record Row(String username, double k, double sum) {}

        var rows = aggs.stream()
                .map(a -> new Row(a.getUsername(), getKoef(a.getScores(), a.getAmountOfGames()), a.getScores()))
                .sorted(Comparator.comparing(Row::k).reversed())
                .toList();

        var t = new TableRenderer()
                .addColumn("Ник", 4)
                .addColumn(" Коэф", 5)
                .addColumn(" Очки", 5);

        for (var r : rows) {
            t.addRow(r.username(),
                    NumFmt.withSign(r.k(),   NumFmt.three(r.k())),
                    NumFmt.withSign(r.sum(), NumFmt.two(r.sum())));
        }
        return t.renderAsInlineCodeLines();
    }

    private String renderGame(List<ScoresDto> rows) {
        int maxNick = rows.stream().mapToInt(s -> s.getUsername().length()).max().orElse(4);
        var t = new TableRenderer()
                .addColumn("Ник", Math.max(4, maxNick))
                .addColumn("Роль", 5)
                .addColumn("\uD83D\uDD25", 5)
                .addColumn("о/п/ж", 7)
                .addColumn("Очки", 5);

        for (var s : rows) {
            t.addRow(s.getUsername(),
                    nonNullOrEmpty(s.getStatus()),
                    NumFmt.two(s.getFlamethrowerScores()),
                    firstOrEmpty(s.getOpjStatusScores()),
                    NumFmt.two(s.getScores()));

            if (s.getOpjStatusScores() != null && s.getOpjStatusScores().size() > 1) {
                for (int i = 1; i < s.getOpjStatusScores().size(); i++) {
                    t.addRow("", "", "", s.getOpjStatusScores().get(i), "");
                }
            }
        }
        return t.renderAsInlineCodeLines();
    }

    private long resolveGameId(long userId) {
        var user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Вы не зарегестрированы"));
        var cached = scoresStateCache.get(userId);
        if (cached != null && !Objects.equals(user.getAuthority(), ROLE_USER)) {
            return cached.getGameId();
        }
        return gameService.findLastGameByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Вы не зарегестрированы ни в одной игре"))
                .getId();
    }

    private static String nonNullOrEmpty(String s) { return s == null ? "" : s; }

    private static String firstOrEmpty(List<String> list) {
        return (list == null || list.isEmpty()) ? "" : list.get(0);
    }

    private static double getKoef(double sum, long count) {
        return sum / Math.sqrt(Math.max(count, 1));
    }

}

