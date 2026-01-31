package nechto.button.role;

import lombok.RequiredArgsConstructor;
import nechto.button.Button;
import nechto.button.ButtonService;
import nechto.cache.ButtonStatusCache;
import nechto.cache.ScoresStateCache;
import nechto.dto.CachedScoresDto;
import nechto.service.InlineKeyboardService;
import nechto.service.ScoresService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.CONTAMINATED_BUTTON;
import static nechto.enums.Button.HUMAN_BUTTON;
import static nechto.enums.Button.LAST_CONTAMINATED_BUTTON;
import static nechto.enums.Button.NECHTO_BUTTON;
import static nechto.utils.BotUtils.getButtonNameWithMessageId;

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
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String nechtoButtonName = getButtonNameWithMessageId(callbackQuery, NECHTO_BUTTON);
        String humanButtonName = getButtonNameWithMessageId(callbackQuery, HUMAN_BUTTON);
        String contaminatedButtonName = getButtonNameWithMessageId(callbackQuery, CONTAMINATED_BUTTON);
        String lastContaminatedButtonName = getButtonNameWithMessageId(callbackQuery, LAST_CONTAMINATED_BUTTON);
        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);

        scoresService.addStatus(buttonStatusCache.getStatus(getButton()), cachedScoresDto.getUserId(), cachedScoresDto.getGameId());
        buttonService.deactivateButtons(nechtoButtonName, humanButtonName, contaminatedButtonName, lastContaminatedButtonName);
        return inlineKeyboardService.returnButtonsForHuman(userId);
    }
}
