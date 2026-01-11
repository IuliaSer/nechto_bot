package nechto.button.role;

import nechto.button.ButtonService;
import nechto.cache.ButtonStatusCache;
import nechto.cache.ScoresStateCache;
import nechto.enums.Button;
import nechto.enums.Status;
import nechto.service.InlineKeyboardService;
import nechto.service.ScoresService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.HUMAN_BUTTON;

@Component
public class HumanButton extends RoleButton {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;

    public HumanButton(ScoresStateCache scoresStateCache, ScoresService scoresService,
                       InlineKeyboardService inlineKeyboardService, ButtonService buttonService, ButtonStatusCache buttonStatusCache) {
        super(scoresStateCache, scoresService, inlineKeyboardService, buttonService, buttonStatusCache);
        this.inlineKeyboardService = inlineKeyboardService;
        this.scoresStateCache = scoresStateCache;
    }

    @Override
    public Button getButton() {
        return HUMAN_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        scoresStateCache.get(userId).setStatus(Status.HUMAN);

        return super.onButtonPressed(callbackQuery, userId) != null ?
                inlineKeyboardService.returnButtonsToAskIfBurned(userId) : null;
    }
}
