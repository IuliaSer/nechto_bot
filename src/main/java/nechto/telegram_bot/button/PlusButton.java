package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static java.lang.String.format;
import static nechto.utils.CommonConstants.END_GAME_BUTTON;
import static nechto.utils.CommonConstants.PLUS_BUTTON;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class PlusButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCash scoresStateCash;

    @Override
    public String getButtonName() {
        return PLUS_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        int messageId = callbackQuery.getMessage().getMessageId();
        int flamethrowerAmount = requestScoresDto.getFlamethrowerAmount();

        requestScoresDto.setFlamethrowerAmount(++flamethrowerAmount);
        return inlineKeyboardService.editeMessageForInlineKeyboardPlusMinus(userId, messageId, format("Выберите количество:\n"),
                flamethrowerAmount);
    }
}
