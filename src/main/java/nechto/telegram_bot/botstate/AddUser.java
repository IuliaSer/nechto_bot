package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.enums.BotState;
import nechto.service.GameService;
import nechto.telegram_bot.cache.ScoresStateCache;
import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class AddUser implements BotStateInterface {
    private final ScoresStateCache scoresStateCache;
    private final GameService gameService;

    @Override
    public BotState getBotState() {
        return BotState.ADD_USER;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        long gameId = Long.parseLong(message.getText().replace("/start add_user_to_game_", ""));
        scoresStateCache.getScoresStateMap().get(userId).setGameId(gameId);
        gameService.addUser(gameId, userId);
        return BotUtils.getSendMessage(userId, "Вы успешно присоединились к игре!");
    }
}
