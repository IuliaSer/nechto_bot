package nechto.button.flamethrower;

import lombok.RequiredArgsConstructor;
import nechto.button.ButtonService;
import nechto.cache.ButtonsCache;
import nechto.dto.CachedScoresDto;
import nechto.service.InlineKeyboardService;
import nechto.button.Button;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.END_COUNT_BUTTON;
import static nechto.enums.Button.FLAMETHROWER_BUTTON;

@RequiredArgsConstructor
@Component
public class FlamethrowerButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;
    private final ButtonsCache buttonsCache;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return FLAMETHROWER_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        String buttonName = getButton().name() + callbackquery.getMessage().getMessageId();
        String endCountButtonName = END_COUNT_BUTTON.name() + callbackquery.getMessage().getMessageId();

        if (buttonsCache.get(buttonName) != null && !buttonsCache.get(buttonName)) {
            return null;
        }
        CachedScoresDto requestScoresDto = scoresStateCache.get(userId);
        requestScoresDto.setFlamethrowerAmount(1);
        buttonService.deactivateButtons(buttonName, endCountButtonName); //add , END_COUNT_BUTTON.name()
        return inlineKeyboardService.getMessageWithInlineMurkupPlusMinus(userId, 1);
    }
}
