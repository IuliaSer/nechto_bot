package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.enums.BotState;
import nechto.enums.Status;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.BotStateCache;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

import static nechto.enums.Button.END_COUNT_BUTTON;
import static nechto.enums.Status.*;

@RequiredArgsConstructor
@Component
public class EndCountButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final ScoresService scoresService;
    private final InlineKeyboardService inlineKeyboardService;
    private final BotStateCache botStateCache;

    @Override
    public nechto.enums.Button getButton() {
        return END_COUNT_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        RequestScoresDto requestScoresDto = scoresStateCache.get(userId);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        int flamethrowerAmount = requestScoresDto.getFlamethrowerAmount();
        int antiHumanFlamethrowerAmount = requestScoresDto.getAntiHumanFlamethrowerAmount();

        for (int i = 0; i < flamethrowerAmount; i++) {
            scoresService.addStatus(FLAMETHROWER, userIdToCount, gameId);
        }

        for (int i = 0; i < antiHumanFlamethrowerAmount; i++) {
            scoresService.addStatus(ANTI_HUMAN_FLAMETHROWER, userIdToCount, gameId);
        }
        List<Status> statuses = scoresService.findByUserIdAndGameId(userIdToCount, gameId).getStatuses();
        if (statuses.contains(BURNED) && statuses.contains(NECHTO)) {
            scoresService.deleteStatus(BURNED, userIdToCount, gameId);
            scoresService.addStatus(LOOSE, userIdToCount, gameId);
        }
        requestScoresDto.setFlamethrowerAmount(0);
        requestScoresDto.setAntiHumanFlamethrowerAmount(0);
        if (botStateCache.get(userId).equals(BotState.CHANGE_GAME)) {
            return inlineKeyboardService.returnButtonsForChangingGame(userId);
        }
        return inlineKeyboardService.returnButtonsToEndGameOrCountNext(userId);
    }
}
