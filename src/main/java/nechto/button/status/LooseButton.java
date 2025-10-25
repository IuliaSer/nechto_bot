package nechto.button.status;

import nechto.button.ButtonService;
import nechto.service.ScoresService;
import nechto.service.InlineKeyboardService;
import nechto.cache.ButtonStatusCache;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.LOOSE_BUTTON;

@Component
public class LooseButton extends StatusButton {

    public LooseButton(ScoresStateCache scoresStateCache, ScoresService scoresService,
                       InlineKeyboardService inlineKeyboardService, ButtonService buttonService, ButtonStatusCache buttonStatusCache) {
        super(scoresStateCache, scoresService, inlineKeyboardService, buttonService, buttonStatusCache);
    }

    @Override
    public nechto.enums.Button getButton() {
        return LOOSE_BUTTON;
    }

}
