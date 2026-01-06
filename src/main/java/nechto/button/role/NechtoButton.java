package nechto.button.role;

import nechto.enums.Button;
import nechto.service.ScoresService;
import nechto.service.InlineKeyboardService;
import nechto.button.ButtonService;
import nechto.cache.ButtonStatusCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import nechto.cache.ScoresStateCache;

import static nechto.enums.Button.*;

@Component
public class NechtoButton extends RoleButton {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;

    public NechtoButton(ScoresStateCache scoresStateCache, ScoresService scoresService, InlineKeyboardService inlineKeyboardService,
                        ButtonService buttonService, ButtonStatusCache buttonStatusCache) {
        super(scoresStateCache, scoresService, inlineKeyboardService, buttonService, buttonStatusCache);
        this.inlineKeyboardService = inlineKeyboardService;
        this.scoresStateCache = scoresStateCache;
    }

    @Override
    public Button getButton() {
        return NECHTO_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        scoresStateCache.get(userId).setNechtoIsChoosen(true);

        return super.onButtonPressed(callbackquery, userId) != null ?
                inlineKeyboardService.returnButtonsForNechto(userId) : null;
    }
}
