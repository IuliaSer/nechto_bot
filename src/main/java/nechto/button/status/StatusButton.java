package nechto.button.status;

import lombok.RequiredArgsConstructor;
import nechto.button.Button;
import nechto.button.ButtonService;
import nechto.dto.CachedScoresDto;
import nechto.service.ScoresService;
import nechto.service.InlineKeyboardService;
import nechto.cache.ButtonStatusCache;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.*;

@RequiredArgsConstructor
@Component
public abstract class StatusButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final ScoresService scoresService;
    private final InlineKeyboardService inlineKeyboardService;
    private final ButtonService buttonService;
    private final ButtonStatusCache buttonStatusCache;

    public abstract nechto.enums.Button getButton();

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().name())) {
            return null;
        }
        buttonService.deactivateButtons(LOOSE_BUTTON.name(), WIN_BUTTON.name(), BURNED_BUTTON.name());

        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long userIdToCount = cachedScoresDto.getUserId();
        long gameId = cachedScoresDto.getGameId();
        scoresService.addStatus(buttonStatusCache.getStatus(getButton()), userIdToCount, gameId);
        return inlineKeyboardService.returnButtonsWithRolesForNechtoWin(userId);
    }
}
