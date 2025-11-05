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
import static nechto.enums.BotState.MAKE_USER;
import static nechto.utils.BotUtils.getSendMessage;

@Component
@RequiredArgsConstructor
public class MakeUser implements BotState {
    private final UserService userService;
    private final MenuService menuService;

    @Override
    public nechto.enums.BotState getBotState() {
        return MAKE_USER;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        String messageText = message.getText();
        ResponseUserDto responseUserDto = userService.findByUsernameOrThrow(messageText);
        long userIdToMakeUser = responseUserDto.getId();
        userService.makeUser(userIdToMakeUser);
        menuService.refreshCommands(userIdToMakeUser, Authority.ROLE_USER);
        return getSendMessage(userId, format("У пользователя %s теперь отсутствуют права админа", messageText));
    }
}
