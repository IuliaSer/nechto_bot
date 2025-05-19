package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Status.WON;
import static nechto.utils.CommonConstants.SCORES;
import static nechto.utils.CommonConstants.WIN_BUTTON;

@RequiredArgsConstructor
@Component
public class WinButton implements Button {
    private final ScoresStateCash scoresStateCash;
    private final ScoresService scoresService;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public String getButtonName() {
        return WIN_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        scoresService.addStatus(WON, userIdToCount, gameId);
        return inlineKeyboardService.returnButtonsWithStatuses(userId);
    }
}
