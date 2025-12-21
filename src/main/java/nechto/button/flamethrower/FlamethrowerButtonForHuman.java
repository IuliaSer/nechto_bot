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

import static nechto.enums.Button.FLAMETHROWER_BUTTON_FOR_HUMAN;

@RequiredArgsConstructor
@Component
public class FlamethrowerButtonForHuman implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return FLAMETHROWER_BUTTON_FOR_HUMAN;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().name())) {
            return null;
        }
        CachedScoresDto requestScoresDto = scoresStateCache.get(userId);
        requestScoresDto.setFlamethrowerAmount(1);
        buttonService.deactivateButtons(getButton().name());

        return inlineKeyboardService
                .getMessageWithInlineMurkupPlusMinusWithAntiHumanFlamethrower(userId, 1);
    }
}
