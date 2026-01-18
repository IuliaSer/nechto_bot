package nechto.button.flamethrower;

import nechto.button.ButtonService;
import nechto.cache.ScoresStateCache;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.FLAMETHROWER_BUTTON;

@Component
public class FlamethrowerForContaminatedAndNechtoButton extends FlamethrowerButton {
    private final InlineKeyboardService inlineKeyboardService;

    public FlamethrowerForContaminatedAndNechtoButton(InlineKeyboardService inlineKeyboardService, ButtonService buttonService,
                                                      ScoresStateCache scoresStateCache) {
        super(buttonService, scoresStateCache);
        this.inlineKeyboardService = inlineKeyboardService;
    }

    @Override
    public nechto.enums.Button getButton() {
        return FLAMETHROWER_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        super.onButtonPressed(callbackQuery, userId);
        return inlineKeyboardService.getMessageWithInlineMurkupPlusMinus(userId, 1);
    }
}
