package nechto.telegram_bot.botstate;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nechto.service.GameService;
import nechto.service.RoleService;
import nechto.service.UserService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static java.lang.String.format;
import static nechto.enums.BotState.COUNT;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class Count implements BotState {
    private final RoleService roleService;
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
        String messageText = message.getText();
        long userIdToCount;

        try {
            userIdToCount = userService.findByUsername(messageText).getId();
            gameService.addUser(scoresStateCache.getScoresStateMap().get(userId).getGameId(), userIdToCount);  //na vremya testa, userIdToCount); //na vremya testa
        } catch (EntityNotFoundException e) {
            return getSendMessage
                    (userId, format("Пользователь с ником %s не существует или не добавлен в игру", messageText)); //TODO разделить в 2 сообщения
        }
//        scoresStateCache.getScoresStateMap().get(userId).setGameId( scoresStateCache.getScoresStateMap().get(userId).getGameId();  //na vremya testa);  //na vremya testa
        scoresStateCache.getScoresStateMap().get(userId).setUserId(userIdToCount);
        return inlineKeyboardService.returnButtonsWithStatuses(userId);
    }
}
