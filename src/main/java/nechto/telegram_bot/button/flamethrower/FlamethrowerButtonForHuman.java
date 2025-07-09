package nechto.telegram_bot.button.flamethrower;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.Button;
import nechto.telegram_bot.cache.ButtonsCash;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.ANTI_HUMAN_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.FLAMETHROWER_BUTTON_FOR_HUMAN;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class FlamethrowerButtonForHuman implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCash scoresStateCash;
    private final ButtonsCash buttonsCash;

    @Override
    public String getButtonName() {
        return FLAMETHROWER_BUTTON_FOR_HUMAN.name();
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        int flamethrowerAmount = requestScoresDto.getFlamethrowerAmount();
        buttonsCash.getButtonMap().get(ANTI_HUMAN_FLAMETHROWER_BUTTON.name()).setActive(false);

        return inlineKeyboardService
                .getMessageWithInlineMurkupPlusMinusWithAntiHumanFlamethrower(userId, flamethrowerAmount);
    }
}
