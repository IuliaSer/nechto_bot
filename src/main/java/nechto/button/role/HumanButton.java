package nechto.button.role;

import nechto.enums.Button;
import nechto.service.ScoresService;
import nechto.button.ButtonService;
import nechto.service.InlineKeyboardService;
import nechto.cache.ButtonStatusCache;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.HUMAN_BUTTON;

@Component
public class HumanButton extends RoleButton {


    public HumanButton(ScoresStateCache scoresStateCache, ScoresService scoresService,
                       InlineKeyboardService inlineKeyboardService, ButtonService buttonService, ButtonStatusCache buttonStatusCache) {
        super(scoresStateCache, scoresService, inlineKeyboardService, buttonService, buttonStatusCache);
    }

    @Override
    public Button getButton() {
        return HUMAN_BUTTON;
    }

}
