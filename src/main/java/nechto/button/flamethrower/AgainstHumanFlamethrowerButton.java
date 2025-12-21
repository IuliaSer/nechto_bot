package nechto.button.flamethrower;

import lombok.RequiredArgsConstructor;
import nechto.button.ButtonService;
import nechto.dto.CachedScoresDto;
import nechto.service.InlineKeyboardService;
import nechto.button.Button;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.ANTI_HUMAN_FLAMETHROWER_BUTTON;

@RequiredArgsConstructor
@Component
public class AgainstHumanFlamethrowerButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return ANTI_HUMAN_FLAMETHROWER_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().name())) {
            return null;
        }
        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        cachedScoresDto.setAntiHumanFlamethrowerAmount(1);
        buttonService.deactivateButtons(getButton().name());
        return inlineKeyboardService.getMessageWithInlineMurkupPlusMinusAntiHuman(userId, 1);
    }
}
