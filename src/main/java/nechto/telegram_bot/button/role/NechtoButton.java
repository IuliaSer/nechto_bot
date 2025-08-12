package nechto.telegram_bot.button.role;

import nechto.enums.Button;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ButtonStatusCache;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.NECHTO_BUTTON;

@Component
public class NechtoButton extends RoleButton {

    public NechtoButton(ScoresStateCache scoresStateCache, ScoresService scoresService,
                        InlineKeyboardService inlineKeyboardService, ButtonService buttonService, ButtonStatusCache buttonStatusCache) {
        super(scoresStateCache, scoresService, inlineKeyboardService, buttonService, buttonStatusCache);
    }

    @Override
    public Button getButton() {
        return NECHTO_BUTTON;
    }

}
