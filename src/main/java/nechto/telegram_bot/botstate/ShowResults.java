package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.dto.ScoresDto;
import nechto.dto.request.RequestScoresDto;
import nechto.entity.Scores;
import nechto.enums.BotState;
import nechto.enums.Status;
import nechto.service.RoleService;
import nechto.service.ScoresService;
import nechto.telegram_bot.cache.ScoresStateCash;
import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;
import static nechto.enums.BotState.CHANGE_GAME;
import static nechto.enums.BotState.SHOW_RESULTS;
import static nechto.enums.Status.CONTAMINATED;
import static nechto.enums.Status.DANGEROUS;
import static nechto.enums.Status.FLAMETHROWER;
import static nechto.enums.Status.HUMAN;
import static nechto.enums.Status.NECHTO;
import static nechto.enums.Status.USEFULL;
import static nechto.enums.Status.VICTIM;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class ShowResults implements BotStateInterface {
    private final RoleService roleService;
    private final ScoresService scoresService;
    private final ScoresStateCash scoresStateCash;

    @Override
    public BotState getBotState() {
        return SHOW_RESULTS;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        roleService.checkIsAdmin(userId);
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        List<Scores> scores = scoresService.findAllByGameId(59L); //test
        List<ScoresDto> scoresDtos = new ArrayList<>();
        float flamethrowerScores = 0;
        Map<ScoresDto, Boolean> opjMap = new ConcurrentHashMap<>();

        for (Scores score: scores) {
            List<String> opjStatusesList = new ArrayList<>();
            ScoresDto scoresDto = new ScoresDto();
            scoresDto.setUsername(score.getUser().getUsername());
            scoresDto.setScores(score.getScores());
            opjMap.put(scoresDto, false);
            for (Status status: score.getStatuses()) {
                if (NECHTO.equals(status) || CONTAMINATED.equals(status) || HUMAN.equals(status)) {
                    scoresDto.setStatus(status.getName());
                }
                if (FLAMETHROWER.equals(status)) {
                    flamethrowerScores += 0.3f;
                }
                if  (DANGEROUS.equals(status)) {
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
                System.out.println(scoresDto.getUsername());
                opjMap.replace(scoresDto, true);
            }
            scoresDto.setOpjStatusScores(opjStatusesList);
            scoresDto.setFlamethrowerScores(flamethrowerScores);
            scoresDtos.add(scoresDto);

            flamethrowerScores = 0;
        }

        return BotUtils.getSendMessageWithMarkDown(userId, formatScoreTable(scoresDtos, opjMap));
    }

    public String formatScoreTable(List<ScoresDto> scoresDtos, Map<ScoresDto, Boolean> opjMap) {
        // Найдём максимальную длину имени для выравнивания
        int maxNameLength = scoresDtos.stream()
                .mapToInt(s -> s.getUsername().length())
                .max()
                .orElse(4);

        StringBuilder sb = new StringBuilder();
        sb.append("`")
                .append(format("%-" + 9 + "s", "Ник"))
                .append(format("%-" + 5 + "s", "Роль"))
                .append(format("%-" + 5 + "s", "Ог-ы"))
                .append(format("%-" + 7 + "s", "о/п/ж"))
                .append(format("Очки"))
                .append("`\n");

        for (ScoresDto s : scoresDtos) {
            String opjScores = "";
            if (!s.getOpjStatusScores().isEmpty()) {
                opjScores = s.getOpjStatusScores().get(0);
            }
            sb.append("`")
                    .append(format("%-" + 9 + "s", s.getUsername()))
                    .append(format("%-" + 5 + "s", s.getStatus()))
                    .append(format("%-" + 5 + "s", s.getFlamethrowerScoresWithPrecision()))
                    .append(format("%-" + 7 + "s", opjScores))
                    .append(format("%.2f", s.getScores()))
                    .append("`\n");

            if (opjMap.get(s)) {
                System.out.println("is opj");

                for (int i = 1; i < s.getOpjStatusScores().size(); i++) {
                    opjScores = s.getOpjStatusScores().get(i);
                    sb.append("`")
                        .append(format("%-" + maxNameLength + 1 + "s", ""))
                        .append(format("%-" + 5 + "s", ""))
                        .append(format("%-" + 5 + "s", ""))
                        .append(format("%-" + 7 + "s", opjScores))
                        .append(format("%s", ""))
                        .append("`\n");
                }
            }
        }

        return sb.toString();
    }
}
