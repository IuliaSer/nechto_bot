package nechto.telegram_bot.button.count;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.Button;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static java.lang.String.format;
import static nechto.enums.Button.MINUS_ANTI_FLAMETHROWER_BUTTON;

@RequiredArgsConstructor
@Component
public class MinusButtonWithAntiFlamethrower implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;

    @Override
    public nechto.enums.Button getButton() {
        return MINUS_ANTI_FLAMETHROWER_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        RequestScoresDto requestScoresDto = scoresStateCache.get(userId);
        int messageId = callbackQuery.getMessage().getMessageId();
        int flamethrowerAmount = requestScoresDto.getFlamethrowerAmount();
        requestScoresDto.setFlamethrowerAmount(--flamethrowerAmount);

        return inlineKeyboardService.editeMessageForInlineKeyboardPlusMinusForAntiHumanFlamethrower(userId, messageId,
                format("Выберите количество:\n"), Math.max(flamethrowerAmount, 0));
    }
}
