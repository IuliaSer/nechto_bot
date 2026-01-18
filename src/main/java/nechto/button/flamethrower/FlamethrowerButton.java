package nechto.button.flamethrower;

import lombok.RequiredArgsConstructor;
import nechto.button.Button;
import nechto.button.ButtonService;
import nechto.cache.ScoresStateCache;
import nechto.dto.CachedScoresDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.AGAINST_HUMAN_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.END_COUNT_BUTTON;
import static nechto.enums.Button.FLAMETHROWER_BUTTON;
import static nechto.enums.Button.FLAMETHROWER_FOR_HUMAN_BUTTON;
import static nechto.utils.BotUtils.getButtonNameWithMessageId;

@RequiredArgsConstructor
@Component
public abstract class FlamethrowerButton implements Button {
    private final ButtonService buttonService;
    private final ScoresStateCache scoresStateCache;

    public abstract nechto.enums.Button getButton();

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = getButtonNameWithMessageId(callbackQuery, getButton());

        if (!buttonService.isActive(buttonName)) {
            return null;
        }
        String flamethrowerButtonName = getButtonNameWithMessageId(callbackQuery, FLAMETHROWER_FOR_HUMAN_BUTTON);
        String flamethrowerForContaminatedButtonName = getButtonNameWithMessageId(callbackQuery, FLAMETHROWER_BUTTON);
        String endCountButtonName = getButtonNameWithMessageId(callbackQuery, END_COUNT_BUTTON);
        String antihumanFlamethrowerButtonName = getButtonNameWithMessageId(callbackQuery, AGAINST_HUMAN_FLAMETHROWER_BUTTON);

        CachedScoresDto requestScoresDto = scoresStateCache.get(userId);
        requestScoresDto.setFlamethrowerAmount(1);
        buttonService.deactivateButtons(flamethrowerButtonName, endCountButtonName, antihumanFlamethrowerButtonName, flamethrowerForContaminatedButtonName);
        return null;
    }
}
