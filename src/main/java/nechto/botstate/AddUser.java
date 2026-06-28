package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.ScoresStateCache;
import nechto.entity.Table;
import nechto.service.GameService;
import nechto.service.TableService;
import nechto.service.UserService;
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
    private final TableService tableService;
    private final GameService gameService;
    private final UserService userService;

    @Override
    public nechto.enums.BotState getBotState() {
        return nechto.enums.BotState.ADD_USER;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        Pattern pattern = Pattern.compile("(?<=id=)\\d+");
        Matcher matcher = pattern.matcher(message.getText());
        long tableId = 0;
        long adminId = 0;
        if (matcher.find()) {
            tableId = Long.parseLong(matcher.group());
        }
        if (matcher.find()) {
            adminId = Long.parseLong(matcher.group());
        }
        long gameId = tableService.findLastGameByTableId(tableId).getId();
        scoresStateCache.get(adminId).setGameId(gameId);

        Table table = tableService.findById(tableId);
        table.getCurrentUsers().add(userService.findById(userId));
        gameService.addUser(gameId, userId);
        tableService.save(table);
        return getSendMessage(userId, "Вы успешно присоединились к игре!");
    }
}
