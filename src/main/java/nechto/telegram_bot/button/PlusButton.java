package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static java.lang.String.format;
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
        int finalFlamethrowerAmount = 0;
        int flamethrowerAmount = requestScoresDto.getFlamethrowerAmount();
        int antiHumanFlamethrowerAmount = requestScoresDto.getAntiHumanFlamethrowerAmount();

        if (requestScoresDto.isFlamethrowerPressed()) {
            requestScoresDto.setFlamethrowerAmount(++flamethrowerAmount);
            finalFlamethrowerAmount = flamethrowerAmount;
        } else if (requestScoresDto.isAntiHumanFlamethrowerPressed()) {
            requestScoresDto.setAntiHumanFlamethrowerAmount(++antiHumanFlamethrowerAmount);
            finalFlamethrowerAmount = antiHumanFlamethrowerAmount;
        }

        return inlineKeyboardService.editeMessageForInlineKeyboardPlusMinus(userId, messageId,
                format("Выберите количество:\n"), finalFlamethrowerAmount);
    }
}
