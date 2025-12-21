package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.cache.ScoresStateCache;
import nechto.dto.CachedScoresDto;
import nechto.dto.response.ResponseUserDto;
import nechto.entity.Scores;
import nechto.enums.CommandStatus;
import nechto.service.InlineKeyboardService;
import nechto.service.ScoresService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

import static nechto.enums.Button.PICKED_BUTTON;

@RequiredArgsConstructor
@Component
public class PickedUserButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresService scoresService;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return PICKED_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = callbackQuery.getData();
        if (!buttonService.isActive(buttonName)) {
            return null;
        }

        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long userIdToCount = Long.parseLong(buttonName.substring(PICKED_BUTTON.name().length()));

        clearStatusesIfGameIsChanging(cachedScoresDto, userIdToCount);

        cachedScoresDto.setUserId(userIdToCount);
        List<ResponseUserDto> users = cachedScoresDto.getUsers();
        users.removeIf(user -> user.getId().equals(userIdToCount));

        buttonService.deactivateAllPickedUserButtons();
        return scoresStateCache.get(userId).getCommandStatus().equals(CommandStatus.NECHTO_WIN) ?
                inlineKeyboardService.returnButtonsWithRolesForNechtoWin(userId) :
                inlineKeyboardService.returnButtonsWithRolesForHumanWin(userId);
    }

    private void clearStatusesIfGameIsChanging(CachedScoresDto cachedScoresDto, long userIdToCount) {
        if (cachedScoresDto.isGameIsFinished()) {
            Scores scores = scoresService.findByUserIdAndGameId(userIdToCount, cachedScoresDto.getGameId());
            scoresService.deleteAllStatuses(scores);
        }
    }
}
