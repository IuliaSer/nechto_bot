package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.entity.Scores;
import nechto.service.RoleService;
import nechto.service.ScoresService;
import nechto.service.UserService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.CHANGE_GAME;

@RequiredArgsConstructor
@Component
public class ChangeGame implements BotState {
    private final RoleService roleService;
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

        Scores scores = scoresService.findByUserIdAndGameId(userIdToCount, 70L);

        scoresService.deleteAllStatuses(scores);
//        long gameId = scoresStateCache.getScoresStateMap().get(userId).getGameId();
        scoresStateCache.getScoresStateMap().get(userId).setGameId(70L);  //na vremya testa
        scoresStateCache.getScoresStateMap().get(userId).setUserId(userIdToCount);
        return inlineKeyboardService.returnButtonsWithStatuses(userId);
    }

}
