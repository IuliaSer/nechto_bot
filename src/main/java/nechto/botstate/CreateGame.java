package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.ScoresStateCache;
import nechto.cache.TableAdminCache;
import nechto.dto.request.RequestGameDto;
import nechto.entity.Table;
import nechto.entity.User;
import nechto.service.GameService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static nechto.enums.BotState.CREATE_GAME;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class CreateGame implements BotState {
    private final GameService gameService;
    private final ScoresStateCache scoresStateCache;
    private final TableAdminCache tableAdminCache;

    @Override
    public nechto.enums.BotState getBotState() {
        return CREATE_GAME;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long adminId = message.getFrom().getId();
        Table table = tableAdminCache.get(adminId);
        RequestGameDto requestGameDto = new RequestGameDto(LocalDateTime.now(), new ArrayList<>(), table);

        long gameId = gameService.save(requestGameDto).getId();

        scoresStateCache.put(adminId);
        scoresStateCache.get(adminId).setGameId(gameId);
        scoresStateCache.get(adminId).setGameIsFinished(false);

        //в игру и за стол добавили игроков
        List<User> users = table.getCurrentUsers();
        gameService.addUsers(gameId, users);

        return getSendMessage(adminId, format("Успешно создана игра"));
    }
}
