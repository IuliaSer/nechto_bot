package nechto.telegram_bot.button.status;

import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ButtonStatusCache;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.WIN_BUTTON;

@Component
public class WinButton extends StatusButton {

    public WinButton(ScoresStateCache scoresStateCache, ScoresService scoresService,
                     InlineKeyboardService inlineKeyboardService, ButtonService buttonService, ButtonStatusCache buttonStatusCache) {
        super(scoresStateCache, scoresService, inlineKeyboardService, buttonService, buttonStatusCache);
    }

    public nechto.enums.Button getButton() {
        return WIN_BUTTON;
    }
}
