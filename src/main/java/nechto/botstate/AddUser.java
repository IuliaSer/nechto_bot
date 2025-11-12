package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.ScoresStateCache;
import nechto.service.GameService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String messageText = message.getText();
        Pattern pattern = Pattern.compile("(?<=id=)\\d+");
        Matcher matcher = pattern.matcher(messageText);
        long gameId = 0;
        long adminId = 0;
        if (matcher.find()) {
            gameId = Long.parseLong(matcher.group());
        }
        if (matcher.find()) {
            adminId = Long.parseLong(matcher.group());
        }
        scoresStateCache.get(adminId).setGameId(gameId);
        gameService.addUser(gameId, userId);
        return getSendMessage(userId, "Вы успешно присоединились к игре!");
    }
}
