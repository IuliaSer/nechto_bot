package nechto.telegram_bot.button.role;

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

import static nechto.enums.Button.CONTAMINATED_BUTTON;
import static nechto.enums.Button.HUMAN_BUTTON;
import static nechto.enums.Button.NECHTO_BUTTON;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public abstract class RoleButton implements Button {
    private final ScoresStateCash scoresStateCash;
    private final ScoresService scoresService;
    private final InlineKeyboardService inlineKeyboardService;
    private final ButtonService buttonService;
    private final ButtonStatusCash buttonStatusCash;

    @Override
    public abstract nechto.enums.Button getButton();

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().getName(), NECHTO_BUTTON.name(), HUMAN_BUTTON.name(), CONTAMINATED_BUTTON.name())) {
            return null;
        }
        Map<nechto.enums.Button, Status> buttonStatusMap = buttonStatusCash.getButtonStatusMap();
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        scoresService.addStatus(buttonStatusMap.get(getButton()), userIdToCount, gameId);
        return inlineKeyboardService.returnButtonsWithAttributesForHuman(userId);
    }
}
