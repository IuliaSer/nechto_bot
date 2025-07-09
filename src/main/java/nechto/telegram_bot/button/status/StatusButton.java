package nechto.telegram_bot.button.status;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.Button;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.CONTAMINATED_BUTTON;
import static nechto.enums.Button.HUMAN_BUTTON;
import static nechto.enums.Button.NECHTO_BUTTON;
import static nechto.enums.Status.HUMAN;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public abstract class StatusButton implements Button {
    private final ScoresStateCash scoresStateCash;
    private final ScoresService scoresService;
    private final InlineKeyboardService inlineKeyboardService;
    private final ButtonService buttonService;

    @Override
    public abstract String getButtonName();

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButtonName(), NECHTO_BUTTON.name(), HUMAN_BUTTON.name(), CONTAMINATED_BUTTON.name())) {
            return null;
        }
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        scoresService.addStatus(getButtonName(), userIdToCount, gameId);
        return inlineKeyboardService.returnButtonsWithAttributesForHuman(userId);
    }
}
