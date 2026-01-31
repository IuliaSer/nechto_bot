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
import static nechto.enums.Button.MINUS_FOR_AGAINST_HUMAN_FLAMETHROWER_BUTTON;

@RequiredArgsConstructor
@Component
public class MinusButtonForAgainstHumanFlamethrower implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;

    @Override
    public nechto.enums.Button getButton() {
        return MINUS_FOR_AGAINST_HUMAN_FLAMETHROWER_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        int messageId = callbackQuery.getMessage().getMessageId();
        int antiHumanFlamethrowerAmount = cachedScoresDto.getAntiHumanFlamethrowerAmount();
        cachedScoresDto.setAntiHumanFlamethrowerAmount(--antiHumanFlamethrowerAmount);

        return inlineKeyboardService.editeMessageForInlineKeyboardPlusMinusForAntiHuman(userId, messageId,
                format("Выберите количество:\n"), Math.max(antiHumanFlamethrowerAmount, 0));
    }
}
