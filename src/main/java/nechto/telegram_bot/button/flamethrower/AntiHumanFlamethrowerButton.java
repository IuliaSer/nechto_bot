package nechto.telegram_bot.button.flamethrower;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.Button;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.ANTI_HUMAN_FLAMETHROWER_BUTTON;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class AntiHumanFlamethrowerButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCash scoresStateCash;
    private final ButtonService buttonService;

    @Override
    public String getButtonName() {
        return ANTI_HUMAN_FLAMETHROWER_BUTTON.name();
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
//        if(!buttonService.isActive(getButtonName())) {
//            return null;
//        }
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        int antiHumanFlamethrowerAmount = requestScoresDto.getAntiHumanFlamethrowerAmount();
        
        return inlineKeyboardService.getMessageWithInlineMurkupPlusMinusAntiHuman(userId, antiHumanFlamethrowerAmount);
    }
}
