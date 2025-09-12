package nechto.telegram_bot.button.role;

import nechto.enums.Button;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ButtonStatusCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import nechto.telegram_bot.cache.ScoresStateCache;

import static nechto.enums.Button.*;

@Component
public class NechtoButton extends RoleButton {
    private final InlineKeyboardService inlineKeyboardService;

    public NechtoButton(ScoresStateCache scoresStateCache, ScoresService scoresService, InlineKeyboardService inlineKeyboardService,
                        ButtonService buttonService, ButtonStatusCache buttonStatusCache) {
        super(scoresStateCache, scoresService, inlineKeyboardService, buttonService, buttonStatusCache);
        this.inlineKeyboardService = inlineKeyboardService;
    }

    @Override
    public Button getButton() {
        return NECHTO_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        super.onButtonPressed(callbackquery, userId);
        return inlineKeyboardService.returnButtonsForNechto(userId);
    }
}
