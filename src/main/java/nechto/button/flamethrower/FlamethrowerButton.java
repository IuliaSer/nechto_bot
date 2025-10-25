package nechto.button.flamethrower;

import lombok.RequiredArgsConstructor;
import nechto.dto.CachedScoresDto;
import nechto.service.InlineKeyboardService;
import nechto.button.Button;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.FLAMETHROWER_BUTTON;

@RequiredArgsConstructor
@Component
public class FlamethrowerButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;
    
    @Override
    public nechto.enums.Button getButton() {
        return FLAMETHROWER_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        CachedScoresDto requestScoresDto = scoresStateCache.get(userId);
        int flamethrowerAmount = requestScoresDto.getFlamethrowerAmount();
        return inlineKeyboardService.getMessageWithInlineMurkupPlusMinus(userId, flamethrowerAmount);
    }
}
