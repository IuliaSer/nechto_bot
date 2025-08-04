package nechto.telegram_bot.button.status;

import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ButtonStatusCash;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.BURNED_BUTTON;

@Component
public class BurnedButton extends StatusButton {

    public BurnedButton(ScoresStateCash scoresStateCash, ScoresService scoresService,
                        InlineKeyboardService inlineKeyboardService, ButtonService buttonService, ButtonStatusCash buttonStatusCash) {
        super(scoresStateCash, scoresService, inlineKeyboardService, buttonService, buttonStatusCash);
    }

    @Override
    public nechto.enums.Button getButton() {
        return BURNED_BUTTON;
    }

}
