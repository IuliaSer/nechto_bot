package nechto.telegram_bot.button.winloose;

import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.BURNED_BUTTON;

@Component
public class BurnedButton extends WinLooseButton {

    public BurnedButton(ScoresStateCash scoresStateCash, ScoresService scoresService, InlineKeyboardService inlineKeyboardService, ButtonService buttonService) {
        super(scoresStateCash, scoresService, inlineKeyboardService, buttonService);
    }

    @Override
    public String getButtonName() {
        return BURNED_BUTTON.name();
    }

}
