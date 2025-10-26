package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.dto.response.ResponseUserDto;
import nechto.enums.Authority;
import nechto.service.MenuService;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static java.lang.String.format;
import static nechto.enums.BotState.MAKE_ADMIN;
import static nechto.utils.BotUtils.getSendMessage;

@Component
@RequiredArgsConstructor
public class MakeAdmin implements BotState {
    private final UserService userService;
    private final MenuService menuService;

    @Override
    public nechto.enums.BotState getBotState() {
        return MAKE_ADMIN;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        final Long chatId = message.getChatId();
        String messageText = message.getText();
        ResponseUserDto responseUserDto = userService.findByUsernameOrThrow(messageText);
        long userIdToMakeAdmin = responseUserDto.getId();
        userService.makeAdmin(userIdToMakeAdmin);
        menuService.refreshCommands(userIdToMakeAdmin, Authority.ROLE_ADMIN);
        return getSendMessage(chatId, format("Пользователь %s теперь является админом", messageText));
    }
}
