package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.entity.Scores;
import nechto.service.ScoresService;
import nechto.service.UserService;
import nechto.service.InlineKeyboardService;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.CHANGE_GAME;

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
        long userIdToCount = userService.findByUsername(message.getText()).getId();
        long gameId = scoresStateCache.get(userId).getGameId();
        Scores scores = scoresService.findByUserIdAndGameId(userIdToCount, gameId);
        scoresService.deleteAllStatuses(scores);

        scoresStateCache.get(userId).setUserId(userIdToCount);

        return inlineKeyboardService.returnButtonsWithCommandStatuses(userId);
    }

}
