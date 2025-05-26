package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.utils.CommonConstants.FLAMETHROWER_BUTTON;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class FlamethrowerButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCash scoresStateCash;
    
    @Override
    public String getButtonName() {
        return FLAMETHROWER_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        requestScoresDto.setFlamethrowerPressed(true);
        requestScoresDto.setAntiHumanFlamethrowerPressed(false);
        int flamethrowerAmount = requestScoresDto.getFlamethrowerAmount();
        return inlineKeyboardService.getMessageWithInlineMurkupPlusMinus(userId, flamethrowerAmount);
    }
}
