package nechto.telegram_bot.button.winloose;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.enums.Status;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.Button;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ButtonStatusCash;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Map;

import static nechto.enums.Button.BURNED_BUTTON;
import static nechto.enums.Button.LOOSE_BUTTON;
import static nechto.enums.Button.WIN_BUTTON;
import static nechto.enums.Status.WON;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public abstract class WinLooseButton implements Button {
    private final ScoresStateCash scoresStateCash;
    private final ScoresService scoresService;
    private final InlineKeyboardService inlineKeyboardService;
    private final ButtonService buttonService;
    private final ButtonStatusCash buttonStatusCash;

    public abstract nechto.enums.Button getButton();

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().name(), LOOSE_BUTTON.name(), WIN_BUTTON.name(), BURNED_BUTTON.name())) {
            return null;
        }
        Map<nechto.enums.Button, Status> buttonStatusMap = buttonStatusCash.getButtonStatusMap();

        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        scoresService.addStatus(buttonStatusMap.get(getButton()), userIdToCount, gameId);
        return inlineKeyboardService.returnButtonsWithStatuses(userId);
    }
}
