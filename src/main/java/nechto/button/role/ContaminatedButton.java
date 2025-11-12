package nechto.button.role;

import nechto.enums.Button;
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

    public ContaminatedButton(ScoresStateCache scoresStateCache, ScoresService scoresService,
                              InlineKeyboardService inlineKeyboardService, ButtonService buttonService,
                              ButtonStatusCache buttonStatusCache) {
        super(scoresStateCache, scoresService, inlineKeyboardService, buttonService, buttonStatusCache);
        this.inlineKeyboardService = inlineKeyboardService;
    }

    @Override
    public Button getButton() {
        return CONTAMINATED_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        return super.onButtonPressed(callbackquery, userId) != null ?
                inlineKeyboardService.returnButtonsForContaminated(userId) : null;
    }
}
