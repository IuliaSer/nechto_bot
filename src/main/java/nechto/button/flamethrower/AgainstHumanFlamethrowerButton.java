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

import static nechto.enums.Button.AGAINST_HUMAN_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.END_COUNT_BUTTON;
import static nechto.enums.Button.FLAMETHROWER_FOR_HUMAN_BUTTON;
import static nechto.enums.Button.MINUS_WITH_AGAINST_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.PLUS_WITH_AGAINST_FLAMETHROWER_BUTTON;
import static nechto.utils.BotUtils.getButtonNameWithMessageId;

@RequiredArgsConstructor
@Component
public class AgainstHumanFlamethrowerButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return AGAINST_HUMAN_FLAMETHROWER_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = getButtonNameWithMessageId(callbackQuery, getButton());
        
        String endCountButtonName = getButtonNameWithMessageId(callbackQuery, END_COUNT_BUTTON);
        String flamethrowerButtonName = getButtonNameWithMessageId(callbackQuery, FLAMETHROWER_FOR_HUMAN_BUTTON);
        String minusWithAgainstHumanFlamethrowerButtonName = getButtonNameWithMessageId(callbackQuery, MINUS_WITH_AGAINST_FLAMETHROWER_BUTTON);
        String plusWithAgainstHumanFlamethrowerButtonName = getButtonNameWithMessageId(callbackQuery, PLUS_WITH_AGAINST_FLAMETHROWER_BUTTON);

        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        cachedScoresDto.setAntiHumanFlamethrowerAmount(1);
        buttonService.deactivateButtons(buttonName, endCountButtonName, flamethrowerButtonName,
                minusWithAgainstHumanFlamethrowerButtonName, plusWithAgainstHumanFlamethrowerButtonName);
        return inlineKeyboardService.getMessageWithInlineMurkupPlusMinusAntiHuman(userId, 1);
    }
}
