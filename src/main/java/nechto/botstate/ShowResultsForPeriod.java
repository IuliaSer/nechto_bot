package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.dto.ShortScoresDto;
import nechto.service.GameService;
import nechto.service.ScoresService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;
import static nechto.enums.BotState.SHOW_RESULTS_FOR_A_PERIOD;
import static nechto.utils.BotUtils.getSendMessageWithMarkDown;
import static nechto.utils.CommonUtils.convertFloatToStringWithTwoDotsPrecision;

@RequiredArgsConstructor
@Component
public class ShowResultsForPeriod implements BotState {
    private final ScoresService scoresService;
    private final GameService gameService;

    @Override
    public nechto.enums.BotState getBotState() {
        return SHOW_RESULTS_FOR_A_PERIOD;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        //parse date 2025,10,26; 2025,11,27
        String[] dates = message.getText().split(";");
        String[] ymdStart = dates[0].split(",");
        int yearsStart = Integer.parseInt(ymdStart[0]);
        int monthsStart = Integer.parseInt(ymdStart[1]);
        int daysStart = Integer.parseInt(ymdStart[2]);
        String[] ymdEnd = dates[1].split(",");
        int yearsEnd = Integer.parseInt(ymdEnd[0]);
        int monthsEnd = Integer.parseInt(ymdEnd[1]);
        int daysEnd = Integer.parseInt(ymdEnd[2]);
        LocalDateTime startPeriod = LocalDateTime.of(yearsStart, monthsStart, daysStart, 0, 0);
        LocalDateTime endPeriod = LocalDateTime.of(yearsEnd, monthsEnd, daysEnd, 0, 0);

        long userId = message.getFrom().getId();
        List<Long> gameIds = gameService.findAllByDate(startPeriod, endPeriod);
        List<ShortScoresDto> scoresDtos = scoresService.findAllByGameIds(gameIds)
                .stream()
                .filter(s -> s.getUser() != null && s.getUser().getId() != null)
                .collect(groupingBy(
                        s -> s.getUser().getUsername(),
                        summingDouble(s -> (double) s.getScores()) // суммируем как double
                ))
                .entrySet().stream()
                .map(e -> new ShortScoresDto(e.getKey(), (float) e.getValue().doubleValue())) // приводим к float
                .sorted(Comparator.comparing(ShortScoresDto::scores).reversed()) // опционально: по убыванию суммы
                .toList();
        return getSendMessageWithMarkDown(userId, formatScoreTable(scoresDtos));
    }

    private String formatScoreTable(List<ShortScoresDto> scoresDtos) {
        int maxNameLength = scoresDtos.stream()
                .mapToInt(s -> s.userName().length() + 1)
                .max()
                .orElse(4);
        StringBuilder sb = new StringBuilder();

        createRow(maxNameLength, sb, "Ник", "Очки");

        for (ShortScoresDto s : scoresDtos) {
            String scores = convertFloatToStringWithTwoDotsPrecision(s.scores());
            createRow(maxNameLength, sb, s.userName(), scores);
        }
        return sb.toString();
    }

    private void createRow(int maxNameLength, StringBuilder sb, String username, String scores) {
        sb.append("`")
                .append(format("%-" + maxNameLength + "s", username))
                .append(format("%s", scores))
                .append("`\n");
    }

}
