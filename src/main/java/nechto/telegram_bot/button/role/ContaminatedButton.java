package nechto.telegram_bot.button.role;

import nechto.enums.Button;
import nechto.service.ScoresService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.ButtonStatusCache;
import nechto.telegram_bot.cache.ScoresStateCache;
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
        super.onButtonPressed(callbackquery, userId);
        return inlineKeyboardService.returnButtonsForContaminated(userId);
    }
}
