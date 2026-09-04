package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.TableAdminCache;
import nechto.entity.User;
import nechto.mappers.UserMapper;
import nechto.service.InlineKeyboardService;
import nechto.service.TableService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static nechto.enums.BotState.DELETE_PLAYER;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class DeletePlayer implements BotState {
    private final TableAdminCache tableAdminCache;
    private final InlineKeyboardService inlineKeyboardService;
    private final UserMapper userMapper;
    private final TableService tableService;

    @Override
    public nechto.enums.BotState getBotState() {
        return DELETE_PLAYER;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long adminId = message.getFrom().getId();
        long tableId = tableAdminCache.get(adminId);

        List<User> users = tableService.findById(tableId).getCurrentUsers();

        return getSendMessage(adminId,
                "Выберите ник игрока, которого надо удалить из последующих игр за этим столом:",
                inlineKeyboardService
                        .returnButtonsWithUsersToDeleteFromTable(userMapper.convertToListOfResponseUserDto(users)));
    }
}
