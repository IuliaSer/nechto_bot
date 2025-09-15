package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.entity.Scores;
import nechto.exception.EntityNotFoundException;
import nechto.service.ScoresService;
import nechto.service.UserService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.CHANGE_GAME;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class ChangeGame implements BotState {
    private final UserService userService;
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;
    private final ScoresService scoresService;

    @Override
    public nechto.enums.BotState getBotState() {
        return CHANGE_GAME;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        Scores scores;
        long userIdToCount;
        try {
            userIdToCount = userService.findByUsername(message.getText()).getId();
            scores = scoresService.findByUserIdAndGameId(userIdToCount, 73L);
        } catch (EntityNotFoundException e) {
            return getSendMessage(userId, "User or game you want to change not found");
        }

        scoresService.deleteAllStatuses(scores);
//        long gameId = scoresStateCache.get(userId).getGameId();
        scoresStateCache.get(userId).setGameId(73L);  //na vremya testa
        scoresStateCache.get(userId).setUserId(userIdToCount);
        return inlineKeyboardService.returnButtonsWithStatuses(userId);
    }

}
