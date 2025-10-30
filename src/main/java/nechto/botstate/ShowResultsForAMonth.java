package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.YearMonth;
import java.util.Locale;

import static nechto.enums.BotState.SHOW_RESULTS_FOR_A_MONTH;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class ShowResultsForAMonth implements BotState {
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public nechto.enums.BotState getBotState() {
        return SHOW_RESULTS_FOR_A_MONTH;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        return getSendMessage(userId, "Выберите месяц:",
                inlineKeyboardService.buildMonthCalendar(userId, YearMonth.now(), Locale.forLanguageTag("ru")));
    }
}
