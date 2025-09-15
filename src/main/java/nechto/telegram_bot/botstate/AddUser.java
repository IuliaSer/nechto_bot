package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.exception.EntityNotFoundException;
import nechto.service.GameService;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.utils.BotUtils.getSendMessage;

@Component
@RequiredArgsConstructor
public class AddUser implements BotState {
    private final ScoresStateCache scoresStateCache;
    private final GameService gameService;

    @Override
    public nechto.enums.BotState getBotState() {
        return nechto.enums.BotState.ADD_USER;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        long gameId = Long.parseLong(message.getText().replace("/start add_user_to_game_", ""));
        scoresStateCache.get(userId).setGameId(gameId);
        try {
            gameService.addUser(gameId, userId);
        } catch (EntityNotFoundException e) {
            return getSendMessage(userId, "Игра или пользователь не найдены!");
        }
        return getSendMessage(userId, "Вы успешно присоединились к игре!");
    }
}
