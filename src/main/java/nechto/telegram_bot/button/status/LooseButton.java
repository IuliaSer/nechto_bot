package nechto.telegram_bot.button.status;

import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ButtonStatusCache;
import nechto.telegram_bot.cache.ScoresStateCache;
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
