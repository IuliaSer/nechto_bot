package nechto.button.role;

import nechto.enums.Button;
import nechto.enums.Status;
import nechto.service.ScoresService;
import nechto.button.ButtonService;
import nechto.service.InlineKeyboardService;
import nechto.cache.ButtonStatusCache;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.CONTAMINATED_BUTTON;

@Component
public class ContaminatedButton extends RoleButton {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;

    public ContaminatedButton(ScoresStateCache scoresStateCache, ScoresService scoresService,
                              InlineKeyboardService inlineKeyboardService, ButtonService buttonService,
                              ButtonStatusCache buttonStatusCache) {
        super(scoresStateCache, scoresService, inlineKeyboardService, buttonService, buttonStatusCache);
        this.inlineKeyboardService = inlineKeyboardService;
        this.scoresStateCache = scoresStateCache;
    }

    @Override
    public Button getButton() {
        return CONTAMINATED_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        scoresStateCache.get(userId).setStatus(Status.CONTAMINATED);
        return super.onButtonPressed(callbackQuery, userId) != null ?
                inlineKeyboardService.returnButtonsToAskIfBurned(userId) : null;
    }
}
