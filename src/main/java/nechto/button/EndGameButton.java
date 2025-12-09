package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.service.ScoresService;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.END_GAME_BUTTON;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class EndGameButton implements Button {
    private final ScoresService scoresService;
    private final ScoresStateCache scoresStateCache;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return END_GAME_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().name())) {
            return null;
        }

        long gameId = scoresStateCache.get(userId).getGameId();

        scoresService.countAndSaveAll(gameId);
        scoresStateCache.get(userId).setGameIsFinished(true);

        buttonService.deactivateAllButtons();

        return getSendMessage(userId, "Успешно посчитано");
    }
}
