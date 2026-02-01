package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.cache.ButtonsCache;
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

import static nechto.enums.Button.PICKED_USER_BUTTON;

@RequiredArgsConstructor
@Component
public class PickedUserButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresService scoresService;
    private final ButtonService buttonService;
    private final ButtonsCache buttonsCache;

    @Override
    public nechto.enums.Button getButton() {
        return PICKED_USER_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = callbackQuery.getData();

        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long userIdToCount = Long.parseLong(buttonName.substring(PICKED_USER_BUTTON.name().length() + 1));

        clearStatusesIfGameIsChanging(cachedScoresDto, userIdToCount);

        cachedScoresDto.setUserId(userIdToCount);
        List<ResponseUserDto> users = cachedScoresDto.getUsers();
        users.removeIf(user -> user.getId().equals(userIdToCount));

        buttonService.deactivateAllPickedUserButtons(callbackQuery);
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
