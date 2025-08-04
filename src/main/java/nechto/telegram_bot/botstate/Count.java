package nechto.telegram_bot.botstate;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nechto.enums.BotState;
import nechto.service.GameService;
import nechto.service.RoleService;
import nechto.service.UserService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.ScoresStateCash;
import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static java.lang.String.format;
import static nechto.enums.BotState.COUNT;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class Count implements BotStateInterface {
    private final RoleService roleService;
    private final GameService gameService;
    private final UserService userService;
    private final ScoresStateCash scoresStateCash;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public BotState getBotState() {
        return COUNT;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        String messageText = message.getText();
        roleService.isAdmin(userId);
        long userIdToCount;
        try {
            userIdToCount = userService.findByUsername(messageText).getId();
            gameService.addUser(59L, userIdToCount); //na vremya testa
        } catch (EntityNotFoundException e) {
            return BotUtils.getSendMessage
                    (userId, format("Пользователь с ником %s не существует или не добавлен в игру", messageText)); //TODO разделить в 2 сообщения
        }
        scoresStateCash.getScoresStateMap().get(SCORES).setGameId(59L);  //na vremya testa
        scoresStateCash.getScoresStateMap().get(SCORES).setUserId(userIdToCount);
        return inlineKeyboardService.returnButtonsWithStatuses(userId);
    }
}
