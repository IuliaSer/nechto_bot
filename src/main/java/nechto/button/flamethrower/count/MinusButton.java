package nechto.button.flamethrower.count;

import lombok.RequiredArgsConstructor;
import nechto.button.Button;
import nechto.cache.ScoresStateCache;
import nechto.dto.CachedScoresDto;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static java.lang.String.format;
import static nechto.enums.Button.MINUS_BUTTON;

@RequiredArgsConstructor
@Component
public class MinusButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;

    @Override
    public nechto.enums.Button getButton() {
        return MINUS_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        int messageId = callbackQuery.getMessage().getMessageId();
        int flamethrowerAmount = cachedScoresDto.getFlamethrowerAmount();
        cachedScoresDto.setFlamethrowerAmount(--flamethrowerAmount);

        return inlineKeyboardService.editeMessageForInlineKeyboardPlusMinus(userId, messageId,
                format("Выберите количество:\n"), Math.max(flamethrowerAmount, 0));
    }
}
