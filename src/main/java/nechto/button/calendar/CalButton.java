package nechto.button.calendar;

import lombok.RequiredArgsConstructor;
import nechto.button.Button;
import nechto.service.results.ShowResultsService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.time.LocalDate;

import static nechto.enums.Button.CAL_BUTTON;

@RequiredArgsConstructor
@Component
public class CalButton implements Button {
    private final ShowResultsService showResultsService;

    @Override
    public nechto.enums.Button getButton() {
        return CAL_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        LocalDate picked = LocalDate.parse(callbackQuery.getData().substring(CAL_BUTTON.name().length() + 1));
        AnswerCallbackQuery ok = new AnswerCallbackQuery();
        ok.setCallbackQueryId(callbackQuery.getId());
        ok.setText("Вы выбрали: " + picked);
        return showResultsService.showResultsForADay("" + picked, userId);
    }
}
