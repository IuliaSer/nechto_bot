package nechto.button.role;

import nechto.button.ButtonService;
import nechto.service.ScoresService;
import nechto.service.InlineKeyboardService;
import nechto.cache.ButtonStatusCache;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.BURNED_BUTTON;

@Component
public class BurnedButton extends RoleButton {
    private final InlineKeyboardService inlineKeyboardService;

    public BurnedButton(ScoresStateCache scoresStateCache, ScoresService scoresService,
                        InlineKeyboardService inlineKeyboardService, ButtonService buttonService, ButtonStatusCache buttonStatusCache) {
        super(scoresStateCache, scoresService, inlineKeyboardService, buttonService, buttonStatusCache);
        this.inlineKeyboardService = inlineKeyboardService;
    }

    @Override
    public nechto.enums.Button getButton() {
        return BURNED_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        return super.onButtonPressed(callbackquery, userId) != null ?
                inlineKeyboardService.returnButtonsForBurned(userId) : null;
    }
}
