package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.cache.ScoresStateCache;
import nechto.dto.CachedScoresDto;
import nechto.enums.CommandStatus;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.PICKED_BUTTON;

@RequiredArgsConstructor
@Component
public class PickedButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public nechto.enums.Button getButton() {
        return PICKED_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {

        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long userIdToCount = Long.parseLong(callbackQuery.getData().substring(PICKED_BUTTON.name().length()));

        if(!cachedScoresDto.isGameIsFinished()) {
            cachedScoresDto.setUserId(userIdToCount);
        }

        return scoresStateCache.get(userId).getCommandStatus().equals(CommandStatus.NECHTO_WIN) ?
                inlineKeyboardService.returnButtonsWithRolesForNechtoWin(userId) :
                inlineKeyboardService.returnButtonsWithRolesForHumanWin(userId);
    }
}
