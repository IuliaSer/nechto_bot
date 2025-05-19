package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Status.CONTAMINATED;
import static nechto.enums.Status.HUMAN;
import static nechto.utils.CommonConstants.END_COUNT_BUTTON;
import static nechto.utils.CommonConstants.HUMAN_BUTTON;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class HumanButton implements Button {
    private final ScoresStateCash scoresStateCash;
    private final ScoresService scoresService;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public String getButtonName() {
        return HUMAN_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        scoresService.addStatus(HUMAN, userIdToCount, gameId);
        return inlineKeyboardService.returnButtonsWithAttributes(userId);

    }
}
