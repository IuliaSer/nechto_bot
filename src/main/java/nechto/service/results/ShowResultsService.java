package nechto.service.results;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface ShowResultsService {
    BotApiMethod<?> showResultsForADay(String ymd, long userId);

    BotApiMethod<?> showResultsForAMonth(String ym, long userId);

    BotApiMethod<?> showResultsForAQuarter(long userId);

    BotApiMethod<?> showResultsForAGame(long userId);
}
