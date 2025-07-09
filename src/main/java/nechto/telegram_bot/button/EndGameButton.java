package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.service.ScoresService;
import nechto.telegram_bot.cache.ScoresStateCash;
import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.END_GAME_BUTTON;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class EndGameButton implements Button {
    private final ScoresService scoresService;
    private final ScoresStateCash scoresStateCash;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return END_GAME_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        long gameId = scoresStateCash.getScoresStateMap().get(SCORES).getGameId();

        scoresService.countAndSaveAll(gameId);
        buttonService.activateAllButtons();
        return BotUtils.getSendMessage(userId, "Успешно посчитано");
    }
}
