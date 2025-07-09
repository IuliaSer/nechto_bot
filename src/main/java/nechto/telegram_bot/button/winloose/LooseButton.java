package nechto.telegram_bot.button.winloose;

import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.LOOSE_BUTTON;

@Component
public class LooseButton extends WinLooseButton {

    public LooseButton(ScoresStateCash scoresStateCash, ScoresService scoresService, InlineKeyboardService inlineKeyboardService, ButtonService buttonService) {
        super(scoresStateCash, scoresService, inlineKeyboardService, buttonService);
    }

    @Override
    public String getButtonName() {
        return LOOSE_BUTTON.name();
    }

}
