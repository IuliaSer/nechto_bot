package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Status.DANGEROUS;
import static nechto.enums.Status.NECHTO;
import static nechto.utils.CommonConstants.DANGEROUS_BUTTON;
import static nechto.utils.CommonConstants.END_COUNT_BUTTON;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class DangerousButton implements Button {
    private final ScoresStateCash scoresStateCash;
    private final ScoresService scoresService;

    @Override
    public String getButtonName() {
        return DANGEROUS_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        scoresService.addStatus(DANGEROUS, userIdToCount, gameId);
        return null;

    }
}
