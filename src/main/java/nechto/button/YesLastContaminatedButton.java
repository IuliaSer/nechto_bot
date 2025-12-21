package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.cache.ScoresStateCache;
import nechto.dto.CachedScoresDto;
import nechto.enums.Status;
import nechto.service.InlineKeyboardService;
import nechto.service.ScoresService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.YES_LAST_CONTAMINATED_BUTTON;

@RequiredArgsConstructor
@Component
public class YesLastContaminatedButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresService scoresService;
    private final ScoresStateCache scoresStateCache;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return YES_LAST_CONTAMINATED_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().name())) {
            return null;
        }
        buttonService.deactivateButtons(getButton().name());

        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long userIdToCount = cachedScoresDto.getUserId();
        long gameId = cachedScoresDto.getGameId();
        scoresService.addStatus(Status.LAST_CONTAMINATED_LOOSE, userIdToCount, gameId);
        return inlineKeyboardService.returnButtonsForContaminated(userId);
    }
}
