package nechto.telegram_bot.button.winloose;

import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ButtonStatusCash;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.WIN_BUTTON;

@Component
public class WinButton extends WinLooseButton {

    public WinButton(ScoresStateCash scoresStateCash, ScoresService scoresService,
                     InlineKeyboardService inlineKeyboardService, ButtonService buttonService, ButtonStatusCash buttonStatusCash) {
        super(scoresStateCash, scoresService, inlineKeyboardService, buttonService, buttonStatusCash);
    }

    public nechto.enums.Button getButton() {
        return WIN_BUTTON;
    }
}
