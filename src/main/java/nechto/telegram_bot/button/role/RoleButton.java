package nechto.telegram_bot.button.role;

import lombok.RequiredArgsConstructor;
import nechto.dto.CachedScoresDto;
import nechto.dto.request.RequestScoresDto;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.Button;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ButtonStatusCache;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.CONTAMINATED_BUTTON;
import static nechto.enums.Button.HUMAN_BUTTON;
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
        if(!buttonService.isActive(getButton().getName())) {
            return null;
        }
        buttonService.deactivateButtons(NECHTO_BUTTON.name(), HUMAN_BUTTON.name(), CONTAMINATED_BUTTON.name());

        CachedScoresDto requestScoresDto = scoresStateCache.get(userId);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        scoresService.addStatus(buttonStatusCache.getStatus(getButton()), userIdToCount, gameId);
        return inlineKeyboardService.returnButtonsForHuman(userId);
    }
}
