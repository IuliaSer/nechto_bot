package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.dto.CachedScoresDto;
import nechto.enums.BotState;
import nechto.enums.CommandStatus;
import nechto.enums.Status;
import nechto.service.ScoresService;
import nechto.service.InlineKeyboardService;
import nechto.cache.BotStateCache;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

import static nechto.enums.Button.END_COUNT_BUTTON;
import static nechto.enums.CommandStatus.NECHTO_WIN;
import static nechto.enums.CommandStatus.PEOPLE_WIN;
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
        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long userIdToCount = cachedScoresDto.getUserId();
        long gameId = cachedScoresDto.getGameId();
        CommandStatus commandStatus = cachedScoresDto.getCommandStatus();
        int flamethrowerAmount = cachedScoresDto.getFlamethrowerAmount();
        int antiHumanFlamethrowerAmount = cachedScoresDto.getAntiHumanFlamethrowerAmount();

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
        if ((commandStatus.equals(PEOPLE_WIN) && statuses.contains(HUMAN)) ||
                (commandStatus.equals(NECHTO_WIN) && !statuses.contains(HUMAN))) {
            scoresService.addStatus(WON, userIdToCount, gameId);
        } else {
            scoresService.addStatus(LOOSE, userIdToCount, gameId);
        }
        cachedScoresDto.setFlamethrowerAmount(0);
        cachedScoresDto.setAntiHumanFlamethrowerAmount(0);
        if (botStateCache.get(userId).equals(BotState.CHANGE_GAME)) {
            return inlineKeyboardService.returnButtonsForChangingGame(userId);
        }
        return inlineKeyboardService.returnButtonsToEndGameOrCountNext(userId);
    }
}
