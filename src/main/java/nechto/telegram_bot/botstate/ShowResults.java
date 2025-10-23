package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.dto.ScoresDto;
import nechto.dto.request.RequestScoresDto;
import nechto.dto.response.ResponseUserDto;
import nechto.entity.Scores;
import nechto.enums.Status;
import nechto.exception.EntityNotFoundException;
import nechto.service.GameService;
import nechto.service.ScoresService;
import nechto.service.UserService;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static nechto.enums.Authority.ROLE_USER;
import static nechto.enums.BotState.SHOW_RESULTS;
import static nechto.enums.Status.CONTAMINATED;
import static nechto.enums.Status.DANGEROUS;
import static nechto.enums.Status.FLAMETHROWER;
import static nechto.enums.Status.HUMAN;
import static nechto.enums.Status.NECHTO;
import static nechto.enums.Status.USEFULL;
import static nechto.enums.Status.VICTIM;
import static nechto.utils.BotUtils.getSendMessageWithMarkDown;
import static nechto.utils.CommonUtils.convertFloatToStringWithTwoDotsPrecision;

@RequiredArgsConstructor
@Component
public class ShowResults implements BotState {
    private final ScoresService scoresService;
    private final GameService gameService;
    private final ScoresStateCache scoresStateCache;
    private final UserService userService;
    private static final int STATUS_LENGTH = 5;
    private static final int FLAMETHROWER_LENGTH = 5;
    private static final int OPJ_LENGTH = 7;

    @Override
    public nechto.enums.BotState getBotState() {
        return SHOW_RESULTS;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        List<ScoresDto> scoresDtos = createScoresDtoForTableRepresentation(userId);
        return getSendMessageWithMarkDown(userId, formatScoreTable(scoresDtos));
    }

    private List<ScoresDto> createScoresDtoForTableRepresentation(long userId) {
        List<ScoresDto> scoresDtos = new ArrayList<>();
        long gameId = getGameId(userId);
        List<Scores> scores = scoresService.findAllByGameId(gameId);

        for (Scores score : scores) {
            float flamethrowerScores = 0;

            List<String> opjStatusesList = new ArrayList<>();

            ScoresDto scoresDto = new ScoresDto();

            for (Status status : score.getStatuses()) {
                if (NECHTO.equals(status) || CONTAMINATED.equals(status) || HUMAN.equals(status)) {
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
        RequestScoresDto requestScoresDto = scoresStateCache.get(userId);

        if (requestScoresDto != null && !responseUserDto.getAuthority().equals(ROLE_USER)) {
            return requestScoresDto.getGameId();
        } else {
            return gameService.findLastGameByUserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Пользователь не добавлен в игру")).getId();
        }
    }

    private String formatScoreTable(List<ScoresDto> scoresDtos) {
        int maxNameLength = scoresDtos.stream()
                .mapToInt(s -> s.getUsername().length() + 1)
                .max()
                .orElse(4);
        StringBuilder sb = new StringBuilder();

        createRow(maxNameLength, sb, "Ник", "Роль", "\uD83D\uDD25", "о/п/ж",
                "Очки");

        for (ScoresDto s : scoresDtos) {
            String opjScores = "";
            if (!s.getOpjStatusScores().isEmpty()) {
                opjScores = s.getOpjStatusScores().get(0);
            }
            String flamethrowerScores = convertFloatToStringWithTwoDotsPrecision(s.getFlamethrowerScores());
            String scores = convertFloatToStringWithTwoDotsPrecision(s.getScores());

            createRow(maxNameLength, sb, s.getUsername(), s.getStatus(), flamethrowerScores, opjScores, scores);

            if (isOpj(s)) {
                for (int i = 1; i < s.getOpjStatusScores().size(); i++) {
                    opjScores = s.getOpjStatusScores().get(i);
                    createRow(maxNameLength, sb, "", "", "", opjScores, "");
                }
            }
        }
        return sb.toString();
    }

    private void createRow(int maxNameLength, StringBuilder sb, String username, String status,
                                    String flamethrower, String opjScores, String scores) {
        sb.append("`")
                .append(format("%-" + maxNameLength + "s", username))
                .append(format("%-" + STATUS_LENGTH + "s", status))
                .append(format("%-" + FLAMETHROWER_LENGTH + "s", flamethrower))
                .append(format("%-" + OPJ_LENGTH + "s", opjScores))
                .append(format("%s", scores))
                .append("`\n");
    }

    private boolean isOpj(ScoresDto scoresDto) {
        return scoresDto.getOpjStatusScores().size() > 1;
    }
}
