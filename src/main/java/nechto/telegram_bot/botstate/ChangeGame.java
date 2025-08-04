package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.entity.Scores;
import nechto.enums.BotState;
import nechto.service.RoleService;
import nechto.service.ScoresService;
import nechto.service.UserService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.CHANGE_GAME;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class ChangeGame implements BotStateInterface {
    private final RoleService roleService;
    private final UserService userService;
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCash scoresStateCash;
    private final ScoresService scoresService;

    @Override
    public BotState getBotState() {
        return CHANGE_GAME;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        long userIdToCount = userService.findByUsername(message.getText()).getId();
        roleService.isAdmin(userId);
        Scores scores = scoresService.findByUserIdAndGameId(userIdToCount, 59L);
        scoresService.deleteAllStatuses(scores);
//        scoresService.deleteById(scores.getId());
        scoresStateCash.getScoresStateMap().get(SCORES).setGameId(59L);  //na vremya testa
        scoresStateCash.getScoresStateMap().get(SCORES).setUserId(userIdToCount);
        return inlineKeyboardService.returnButtonsWithStatuses(userId);
    }

}
