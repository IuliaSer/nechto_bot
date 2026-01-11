package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.exception.FlamethrowerDisbalanceException;
import nechto.service.ScoresService;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.END_GAME_BUTTON;
import static nechto.utils.BotUtils.getButtonNameWithMessageId;
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
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = getButtonNameWithMessageId(callbackQuery, getButton());

        if (!buttonService.isActive(buttonName)) {
            return null;
        }

        long gameId = scoresStateCache.get(userId).getGameId();

        try {
            scoresService.countAndSaveAll(gameId);
        } catch (FlamethrowerDisbalanceException e) {
            return getSendMessage(userId, e.getMessage());
        }

        scoresStateCache.get(userId).setGameIsFinished(true);

        buttonService.deactivateButtons(buttonName);

        return getSendMessage(userId, "Успешно посчитано");
    }
}
