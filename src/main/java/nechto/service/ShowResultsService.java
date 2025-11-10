package nechto.service;

import lombok.RequiredArgsConstructor;
import nechto.cache.ScoresStateCache;
import nechto.dto.CachedScoresDto;
import nechto.dto.ScoresDto;
import nechto.dto.response.ResponseUserDto;
import nechto.entity.Scores;
import nechto.enums.Status;
import nechto.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static nechto.enums.Authority.ROLE_USER;
import static nechto.enums.Status.CONTAMINATED;
import static nechto.enums.Status.DANGEROUS;
import static nechto.enums.Status.FLAMETHROWER;
import static nechto.enums.Status.HUMAN;
import static nechto.enums.Status.LAST_CONTAMINATED;
import static nechto.enums.Status.NECHTO;
import static nechto.enums.Status.USEFULL;
import static nechto.enums.Status.VICTIM;
import static nechto.utils.BotUtils.getSendMessageWithMarkDown;
import static nechto.utils.CommonUtils.addSpaceToPositiveNum;
import static nechto.utils.CommonUtils.convertFloatToStringWithThreeDotsPrecision;
import static nechto.utils.CommonUtils.convertFloatToStringWithTwoDotsPrecision;

@RequiredArgsConstructor
@Service
public class ShowResultsService {
    private final GameService gameService;
    private final ScoresService scoresService;
    private final ScoresStateCache scoresStateCache;
    private final UserService userService;
    private static final int STATUS_LENGTH = 5;
    private static final int FLAMETHROWER_LENGTH = 5;
    private static final int OPJ_LENGTH = 7;
    private static final int KOEF_LENGTH = 7;


    public BotApiMethod<?> showResultsForADay(String message, long userId) {
        String[] ymdStart = message.split("-");
        int yearsStart = Integer.parseInt(ymdStart[0]);
        int monthsStart = Integer.parseInt(ymdStart[1]);
        int daysStart = Integer.parseInt(ymdStart[2]);
        LocalDateTime startDate = LocalDateTime.of(yearsStart, monthsStart, daysStart, 0, 0);
        LocalDateTime endDate = startDate.plusHours(27);
        return getSendMessageWithMarkDown(userId,
                formatScoreTableForDayAndMonth(createScoresDtoForTableQuarterRepresentation(startDate, endDate),"Очки"));

    }

    public BotApiMethod<?> showResultsForAMonth(String message, long userId) {
        String[] ymdStart = message.split("-");
        int yearsStart = Integer.parseInt(ymdStart[0]);
        int monthsStart = Integer.parseInt(ymdStart[1]);
        LocalDateTime startDate = LocalDateTime.of(yearsStart, monthsStart, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1);
        return getSendMessageWithMarkDown(userId,
                formatScoreTableForDayAndMonth(createScoresDtoForTableQuarterRepresentation(startDate, endDate), "Очки"));

    }

    public BotApiMethod<?> showResultsForAQuarter(long userId) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime firstDayOfCurrentQ = today.with(IsoFields.DAY_OF_QUARTER, 1).withHour(0).withMinute(0);

        return getSendMessageWithMarkDown(userId,
                formatScoreTableForQuarter(createScoresDtoForTableQuarterRepresentation(firstDayOfCurrentQ, today),
                        format("%-" + KOEF_LENGTH  + "s", "Коэф"),
                        "Очки"
                        ));
    }

    public BotApiMethod<?> showResultsForAGame(long userId) {
        List<ScoresDto> scoresDtos = createScoresDtoForOneGameTableRepresentation(userId);
        return getSendMessageWithMarkDown(userId, formatScoreTableForAGame(scoresDtos));
    }

    private List<ScoresDto> createScoresDtoForOneGameTableRepresentation(long userId) {
        List<ScoresDto> scoresDtos = new ArrayList<>();
        long gameId = getGameId(userId);
        List<Scores> scores = scoresService.findAllByGameId(gameId);

        for (Scores score : scores) {
            float flamethrowerScores = 0;
            List<String> opjStatusesList = new ArrayList<>();
            ScoresDto scoresDto = ScoresDto.builder().build();

            for (Status status : score.getStatuses()) {
                if (NECHTO.equals(status) || CONTAMINATED.equals(status) || HUMAN.equals(status) || LAST_CONTAMINATED.equals(status)) {
                    scoresDto.setStatus(status.getName());
                }
                if (FLAMETHROWER.equals(status)) {
                    flamethrowerScores += 0.3f;
                }
                if (DANGEROUS.equals(status) || USEFULL.equals(status) || VICTIM.equals(status)) {
                    opjStatusesList.add(status.getName());
                }
            }
            scoresDto.setUsername(score.getUser().getUsername());
            scoresDto.setScores(score.getScores());
            scoresDto.setOpjStatusScores(opjStatusesList);
            scoresDto.setFlamethrowerScores(flamethrowerScores);
            scoresDtos.add(scoresDto);
        }
        return scoresDtos;
    }

    private long getGameId(long userId) {
        ResponseUserDto responseUserDto = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Вы не зарегестрированы"));
        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);

        if (cachedScoresDto != null && !responseUserDto.getAuthority().equals(ROLE_USER)) {
            return cachedScoresDto.getGameId();
        } else {
            return gameService.findLastGameByUserId(userId) //он может быть админом но играть в другой игре в качестве пользователя
                    .orElseThrow(() -> new EntityNotFoundException("Вы не зарегестрированы ни в одной игре")).getId();
        }
    }

    private String formatScoreTableForAGame(List<ScoresDto> scoresDtos) {
        int maxNameLength = getMaxNameLength(scoresDtos);
        StringBuilder sb = new StringBuilder();

        createRowForGameStatistic(maxNameLength, sb, "Ник", "Роль", "\uD83D\uDD25",
                "о/п/ж", "Очки");

        for (ScoresDto s : scoresDtos) {
            String opjScores = "";
            if (!s.getOpjStatusScores().isEmpty()) {
                opjScores = s.getOpjStatusScores().get(0);
            }
            String flamethrowerScores = convertFloatToStringWithTwoDotsPrecision(s.getFlamethrowerScores());
            String scores = convertFloatToStringWithTwoDotsPrecision(s.getScores());
            createRowForGameStatistic(maxNameLength, sb, s.getUsername(), s.getStatus(), flamethrowerScores, opjScores, scores);

            if (isOpj(s)) {
                for (int i = 1; i < s.getOpjStatusScores().size(); i++) {
                    opjScores = s.getOpjStatusScores().get(i);
                    createRowForGameStatistic(maxNameLength, sb, "", "", "", opjScores, "");
                }
            }
        }
        return sb.toString();
    }

    private String formatScoreTableForDayAndMonth(List<ScoresDto> scoresDtos, String ...headers) {
        int maxNameLength = getMaxNameLength(scoresDtos);
        StringBuilder sb = new StringBuilder();

        createHeaderRowWithNick(sb, maxNameLength, headers);

        for (ScoresDto s : scoresDtos) {
            String scores = addSpaceToPositiveNum(s.getScores(), convertFloatToStringWithTwoDotsPrecision(s.getScores()));
            createRow(sb, format("%-" + maxNameLength + "s", s.getUsername()),
                    scores);
        }
        return sb.toString();
    }

    private String formatScoreTableForQuarter(List<ScoresDto> scoresDtos, String ...headers) {
        int maxNameLength = getMaxNameLength(scoresDtos);
        StringBuilder sb = new StringBuilder();

        createHeaderRowWithNick(sb, maxNameLength, headers);

        for (ScoresDto s : scoresDtos) {
            String scores = addSpaceToPositiveNum(s.getScores(), convertFloatToStringWithTwoDotsPrecision(s.getScores()));
            String koef = addSpaceToPositiveNum(s.getKoef(), convertFloatToStringWithThreeDotsPrecision(s.getKoef()));
            createRow(sb, format("%-" + maxNameLength + "s", s.getUsername()),
                    format("%-" + KOEF_LENGTH + "s", koef),
                    scores);
        }
        return sb.toString();
    }

    private boolean isOpj(ScoresDto scoresDto) {
        return scoresDto.getOpjStatusScores().size() > 1;
    }
    
    private List<ScoresDto> createScoresDtoForTableQuarterRepresentation(LocalDateTime dayStart, LocalDateTime dayEnd) {
        List<Long> gameIds = gameService.findAllByDate(dayStart, dayEnd);

        Map<String, List<Scores>> byUser = scoresService.findAllByGameIds(gameIds).stream()
                .filter(Objects::nonNull)
                .filter(s -> s.getUser() != null && s.getUser().getUsername() != null)
                .collect(groupingBy(s -> s.getUser().getUsername()));

        return byUser.entrySet().stream()
                .map(e -> {
                    String username = e.getKey();
                    List<Scores> userScores = e.getValue();

                    double sum = userScores.stream()
                            .mapToDouble(Scores::getScores)
                            .sum();
                    double koef = sum / Math.sqrt(Math.max(userScores.size(), 1)); // защита от деления на 0

                    return ScoresDto.builder()
                            .username(username)
                            .scores((float) sum)
                            .koef((float) koef)
                            .build();
                })
                .sorted(Comparator.comparing(ScoresDto::getKoef).reversed())
                .toList();
    }

    private void createRowForGameStatistic(int maxNameLength, StringBuilder sb, String username, String status,
                           String flamethrower, String opjScores, String scores) {
        createRow(sb,
                format("%-" + maxNameLength + "s", username),
                format("%-" + STATUS_LENGTH + "s", status),
                format("%-" + FLAMETHROWER_LENGTH + "s", flamethrower),
                format("%-" + OPJ_LENGTH + "s", opjScores),
                scores);
    }

    private void createHeaderRowWithNick(StringBuilder sb, int maxNameLength, String... row) {
        createRow(sb, appendToBeginning(row, (format("%-" + (maxNameLength + 1) + "s", "Ник"))));
    }

    private void createRow(StringBuilder sb, String... row) {
        sb.append("`");
        for (String e : row) {
            if (e == null) {
                sb.append("");
            }
            if (sb.length() > 0) {
                sb.append(e);
            }
        }
        sb.append("`\n");
    }

    String[] appendToBeginning(String[] arr, String s) {
        String[] res = Arrays.copyOf(arr, arr.length + 1);
        res[0] = s;
        System.arraycopy(arr, 0, res, 1, arr.length);
        return res;
    }

    private int getMaxNameLength(List<ScoresDto> scoresDtos) {
        return scoresDtos.stream()
                .mapToInt(s -> s.getUsername().length() + 1)
                .max()
                .orElse(4);
    }
}
