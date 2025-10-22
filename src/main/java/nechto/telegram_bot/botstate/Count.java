package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.service.GameService;
import nechto.service.UserService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.COUNT;

@RequiredArgsConstructor
@Component
public class Count implements BotState {
    private final GameService gameService;
    private final UserService userService;
    private final ScoresStateCache scoresStateCache;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public nechto.enums.BotState getBotState() {
        return COUNT;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        String userName = message.getText();
        long userIdToCount;

        userIdToCount = userService.findByUsername(userName).getId();
        Long gameId = scoresStateCache.get(userId).getGameId(); //TODO reshit dobavlya li v addUser?
        gameService.addUser(gameId, userIdToCount);  //TODO na vremya testa, userIdToCount)
        scoresStateCache.get(userId).setUserId(userIdToCount);
        return inlineKeyboardService.returnButtonsWithStatuses(userId);
    }
}
