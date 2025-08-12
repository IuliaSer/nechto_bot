package nechto.telegram_bot.button.role;

import nechto.enums.Button;
import nechto.service.ScoresService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.ButtonStatusCache;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.CONTAMINATED_BUTTON;

@Component
public class ContaminatedButton extends RoleButton {

    public ContaminatedButton(ScoresStateCache scoresStateCache, ScoresService scoresService,
                              InlineKeyboardService inlineKeyboardService, ButtonService buttonService, ButtonStatusCache buttonStatusCache) {
        super(scoresStateCache, scoresService, inlineKeyboardService, buttonService, buttonStatusCache);
    }

    @Override
    public Button getButton() {
        return CONTAMINATED_BUTTON;
    }

}
