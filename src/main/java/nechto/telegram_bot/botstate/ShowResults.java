package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.dto.ScoresDto;
import nechto.dto.request.RequestScoresDto;
import nechto.dto.response.ResponseUserDto;
import nechto.entity.Scores;
import nechto.enums.Authority;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;
import static nechto.enums.BotState.SHOW_RESULTS;
import static nechto.enums.Status.CONTAMINATED;
import static nechto.enums.Status.DANGEROUS;
import static nechto.enums.Status.FLAMETHROWER;
import static nechto.enums.Status.HUMAN;
import static nechto.enums.Status.NECHTO;
import static nechto.enums.Status.USEFULL;
import static nechto.enums.Status.VICTIM;
import static nechto.utils.BotUtils.getSendMessage;
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
        List<ScoresDto> scoresDtos = new ArrayList<>();
        Map<ScoresDto, Boolean> opjMap = new ConcurrentHashMap<>();

        try {
            long gameId;
            List<Scores> scores;
            float flamethrowerScores = 0;

            ResponseUserDto responseUserDto = userService.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User is not registered"));
            RequestScoresDto requestScoresDto = scoresStateCache.get(userId);

            if (requestScoresDto != null && !responseUserDto.getAuthority().equals(Authority.ROLE_USER)) {
                gameId = requestScoresDto.getGameId();
            } else {
                gameId = gameService.findLastGameByUserId(userId)
                        .orElseThrow(() -> new EntityNotFoundException("No game for this user")).getId();
            }
            scores = scoresService.findAllByGameId(gameId);  //na vremya testa

            for (Scores score : scores) {
                List<String> opjStatusesList = new ArrayList<>();
                ScoresDto scoresDto = new ScoresDto();
                scoresDto.setUsername(score.getUser().getUsername());
                scoresDto.setScores(score.getScores());
                opjMap.put(scoresDto, false);
                for (Status status : score.getStatuses()) {
                    if (NECHTO.equals(status) || CONTAMINATED.equals(status) || HUMAN.equals(status)) {
                        scoresDto.setStatus(status.getName());
                    }
                    if (FLAMETHROWER.equals(status)) {
                        flamethrowerScores += 0.3f;
                    }
                    if (DANGEROUS.equals(status)) {
                        opjStatusesList.add("0.2(о)");
                    }
                    if (USEFULL.equals(status)) {
                        opjStatusesList.add("0.2(п)");
                    }
                    if (VICTIM.equals(status)) {
                        opjStatusesList.add("0.5(ж)");
                    }
                }
                if (opjStatusesList.size() > 1) {
                    opjMap.replace(scoresDto, true);
                }
                scoresDto.setOpjStatusScores(opjStatusesList);
                scoresDto.setFlamethrowerScores(flamethrowerScores);
                scoresDtos.add(scoresDto);

                flamethrowerScores = 0;
            }
        } catch (EntityNotFoundException e) {
            return getSendMessage(userId, e.getMessage());
        }

        return getSendMessageWithMarkDown(userId, formatScoreTable(scoresDtos, opjMap));
    }

    public String formatScoreTable(List<ScoresDto> scoresDtos, Map<ScoresDto, Boolean> opjMap) {
        int maxNameLength = scoresDtos.stream()
                .mapToInt(s -> s.getUsername().length())
                .max()
                .orElse(4);
        maxNameLength++;
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

            if (isOpj(opjMap, s)) {
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

    private boolean isOpj(Map<ScoresDto, Boolean> opjMap, ScoresDto scoresDto) {
        return opjMap.get(scoresDto);
    }
}
