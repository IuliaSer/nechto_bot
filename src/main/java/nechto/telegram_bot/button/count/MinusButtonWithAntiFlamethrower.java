package nechto.telegram_bot.button.count;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.Button;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static java.lang.String.format;
import static nechto.enums.Button.MINUS_ANTI_FLAMETHROWER_BUTTON;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class MinusButtonWithAntiFlamethrower implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCash scoresStateCash;

    @Override
    public String getButtonName() {
        return MINUS_ANTI_FLAMETHROWER_BUTTON.name();
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        int messageId = callbackQuery.getMessage().getMessageId();
        int flamethrowerAmount = requestScoresDto.getFlamethrowerAmount();
        requestScoresDto.setFlamethrowerAmount(--flamethrowerAmount);

        return inlineKeyboardService.editeMessageForInlineKeyboardPlusMinusForAntiHumanFlamethrower(userId, messageId,
                format("Выберите количество:\n"), Math.max(flamethrowerAmount, 0));
    }
}
