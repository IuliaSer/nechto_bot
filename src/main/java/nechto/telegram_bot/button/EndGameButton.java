package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.service.ScoresService;
import nechto.telegram_bot.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.utils.BotUtils.getSendMessageWithInlineMarkup;
import static nechto.utils.CommonConstants.END_GAME_BUTTON;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class EndGameButton implements Button {
    private final ScoresService scoresService;
    private final ScoresStateCash scoresStateCash;

    @Override
    public String getButtonName() {
        return END_GAME_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        long gameId = scoresStateCash.getScoresStateMap().get(SCORES).getGameId();

        scoresService.countAndSaveAllScoresInTheGame(gameId);
        return getSendMessageWithInlineMarkup(userId, "Успешно посчитано");
    }
}
