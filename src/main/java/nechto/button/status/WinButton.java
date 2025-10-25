package nechto.button.status;

import nechto.button.ButtonService;
import nechto.service.ScoresService;
import nechto.service.InlineKeyboardService;
import nechto.cache.ButtonStatusCache;
import nechto.cache.ScoresStateCache;
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
