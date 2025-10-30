package nechto.service;

import lombok.RequiredArgsConstructor;
import nechto.dto.ShortScoresDto;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;
import static nechto.utils.BotUtils.getSendMessageWithMarkDown;
import static nechto.utils.CommonUtils.convertFloatToStringWithTwoDotsPrecision;

@RequiredArgsConstructor
@Service
public class ShowResultsService {
    private final GameService gameService;
    private final ScoresService scoresService;

    public BotApiMethod<?> showResultsForADay(String message, long userId) {
        String[] ymdStart = message.split("-");
        int yearsStart = Integer.parseInt(ymdStart[0]);
        int monthsStart = Integer.parseInt(ymdStart[1]);
        int daysStart = Integer.parseInt(ymdStart[2]);
        LocalDateTime dayStart = LocalDateTime.of(yearsStart, monthsStart, daysStart, 0, 0);
        LocalDateTime dayEnd = dayStart.plusHours(27);
        return showResultsForAPeriod(dayStart, dayEnd, userId);
    }

    public BotApiMethod<?> showResultsForAMonth(String message, long userId) {
        String[] ymdStart = message.split("-");
        int yearsStart = Integer.parseInt(ymdStart[0]);
        int monthsStart = Integer.parseInt(ymdStart[1]);
        LocalDateTime startDate = LocalDateTime.of(yearsStart, monthsStart, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1);
        return showResultsForAPeriod(startDate, endDate, userId);
    }

    public BotApiMethod<?> showResultsForAQuarter(long userId) {
        LocalDateTime endDate = LocalDateTime.now();
        int currentMonth = endDate.getMonth().getValue();
        int monthInQuarter = currentMonth % 3;
        int minusMonth;
        if (monthInQuarter == 0) {
            minusMonth = 2;
        } else if (monthInQuarter == 1) {
            minusMonth = 0;
        } else {
            minusMonth = 1;
        }
        int monthStart = currentMonth - minusMonth;
        LocalDateTime startDate = LocalDateTime.of(endDate.getYear(), monthStart, 1, 0, 0);

        return showResultsForAPeriod(startDate, endDate, userId);
    }

    private BotApiMethod<?> showResultsForAPeriod(LocalDateTime dayStart, LocalDateTime dayEnd, long userId) {

        List<Long> gameIds = gameService.findAllByDate(dayStart, dayEnd);
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
