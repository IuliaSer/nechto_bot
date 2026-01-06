package nechto.button.role;

import lombok.RequiredArgsConstructor;
import nechto.dto.CachedScoresDto;
import nechto.service.ScoresService;
import nechto.service.InlineKeyboardService;
import nechto.button.Button;
import nechto.button.ButtonService;
import nechto.cache.ButtonStatusCache;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.BURNED_BUTTON;
import static nechto.enums.Button.CONTAMINATED_BUTTON;
import static nechto.enums.Button.HUMAN_BUTTON;
import static nechto.enums.Button.LAST_CONTAMINATED_BUTTON;
import static nechto.enums.Button.NECHTO_BUTTON;

@RequiredArgsConstructor
@Component
public abstract class RoleButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final ScoresService scoresService;
    private final InlineKeyboardService inlineKeyboardService;
    private final ButtonService buttonService;
    private final ButtonStatusCache buttonStatusCache;

    @Override
    public abstract nechto.enums.Button getButton();

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().name())) {
            return null;
        }
        buttonService.deactivateButtons(NECHTO_BUTTON.name(), HUMAN_BUTTON.name(), CONTAMINATED_BUTTON.name(),
                 LAST_CONTAMINATED_BUTTON.name());

        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long userIdToCount = cachedScoresDto.getUserId();
        scoresService.addStatus(buttonStatusCache.getStatus(getButton()), userIdToCount, cachedScoresDto.getGameId());
        return inlineKeyboardService.returnButtonsForHuman(userId);
    }
}
