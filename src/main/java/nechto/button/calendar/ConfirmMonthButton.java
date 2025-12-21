package nechto.button.calendar;

import lombok.RequiredArgsConstructor;
import nechto.button.Button;
import nechto.service.results.ShowResultsService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.CONFIRM_MONTH_BUTTON;

@RequiredArgsConstructor
@Component
public class ConfirmMonthButton implements Button {
    private final ShowResultsService showResultsService;

    @Override
    public nechto.enums.Button getButton() {
        return CONFIRM_MONTH_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String yearMonth = callbackQuery.getData().substring(getButton().name().length() + 1);
        return showResultsService.showResultsForAMonth(yearMonth, userId);
    }
}
