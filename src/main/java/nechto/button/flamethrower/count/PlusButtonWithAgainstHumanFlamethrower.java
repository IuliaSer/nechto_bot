package nechto.button.flamethrower.count;

import lombok.RequiredArgsConstructor;
import nechto.dto.CachedScoresDto;
import nechto.service.InlineKeyboardService;
import nechto.button.Button;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static java.lang.String.format;
import static nechto.enums.Button.PLUS_ANTI_FLAMETHROWER_BUTTON;

@RequiredArgsConstructor
@Component
public class PlusButtonWithAgainstHumanFlamethrower implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;

    @Override
    public nechto.enums.Button getButton() {
        return PLUS_ANTI_FLAMETHROWER_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        CachedScoresDto requestScoresDto = scoresStateCache.get(userId);
        int messageId = callbackQuery.getMessage().getMessageId();
        int flamethrowerAmount = requestScoresDto.getFlamethrowerAmount();
        requestScoresDto.setFlamethrowerAmount(++flamethrowerAmount);

        return inlineKeyboardService.editeMessageForInlineKeyboardPlusMinusForAntiHumanFlamethrower(userId, messageId,
                format("Выберите количество:\n"), flamethrowerAmount);
    }
}
