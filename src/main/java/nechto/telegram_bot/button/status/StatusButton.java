package nechto.telegram_bot.button.status;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.enums.Status;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.Button;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ButtonStatusCache;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Map;

import static nechto.enums.Button.BURNED_BUTTON;
import static nechto.enums.Button.LOOSE_BUTTON;
import static nechto.enums.Button.WIN_BUTTON;

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
        if(!buttonService.isActive(getButton().name(), LOOSE_BUTTON.name(), WIN_BUTTON.name(), BURNED_BUTTON.name())) {
            return null;
        }
        Map<nechto.enums.Button, Status> buttonStatusMap = buttonStatusCache.getButtonStatusMap();

        RequestScoresDto requestScoresDto = scoresStateCache.getScoresStateMap().get(userId);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        scoresService.addStatus(buttonStatusMap.get(getButton()), userIdToCount, gameId);
        return inlineKeyboardService.returnButtonsWithRoles(userId);
    }
}
