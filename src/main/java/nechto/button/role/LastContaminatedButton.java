package nechto.button.role;

import nechto.button.ButtonService;
import nechto.cache.ButtonStatusCache;
import nechto.cache.ScoresStateCache;
import nechto.enums.Button;
import nechto.service.InlineKeyboardService;
import nechto.service.ScoresService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.LAST_CONTAMINATED_BUTTON;

@Component
public class LastContaminatedButton extends RoleButton {
    private final InlineKeyboardService inlineKeyboardService;

    public LastContaminatedButton(ScoresStateCache scoresStateCache, ScoresService scoresService,
                                  InlineKeyboardService inlineKeyboardService, ButtonService buttonService,
                                  ButtonStatusCache buttonStatusCache) {
        super(scoresStateCache, scoresService, inlineKeyboardService, buttonService, buttonStatusCache);
        this.inlineKeyboardService = inlineKeyboardService;
    }

    @Override
    public Button getButton() {
        return LAST_CONTAMINATED_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        super.onButtonPressed(callbackquery, userId);
        return inlineKeyboardService.returnButtonsForContaminated(userId);
    }
}
