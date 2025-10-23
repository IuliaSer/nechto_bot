package nechto.telegram_bot.button.flamethrower;

import lombok.RequiredArgsConstructor;
import nechto.dto.CachedScoresDto;
import nechto.dto.request.RequestScoresDto;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.Button;
import nechto.telegram_bot.cache.ButtonsCache;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.ANTI_HUMAN_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.FLAMETHROWER_BUTTON_FOR_HUMAN;

@RequiredArgsConstructor
@Component
public class FlamethrowerButtonForHuman implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;
    private final ButtonsCache buttonsCache;

    @Override
    public nechto.enums.Button getButton() {
        return FLAMETHROWER_BUTTON_FOR_HUMAN;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        CachedScoresDto requestScoresDto = scoresStateCache.get(userId);
        int flamethrowerAmount = requestScoresDto.getFlamethrowerAmount();
        buttonsCache.get(ANTI_HUMAN_FLAMETHROWER_BUTTON.getName()).setActive(false); //?

        return inlineKeyboardService
                .getMessageWithInlineMurkupPlusMinusWithAntiHumanFlamethrower(userId, flamethrowerAmount);
    }
}
