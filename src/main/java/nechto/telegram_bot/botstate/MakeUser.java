package nechto.telegram_bot.botstate;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nechto.dto.response.ResponseUserDto;
import nechto.enums.Authority;
import nechto.service.MenuService;
import nechto.service.RoleService;
import nechto.service.UserService;
import nechto.utils.BotUtils;
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
        final Long chatId = message.getChatId();
        String messageText = message.getText();
        try {
            ResponseUserDto responseUserDto = userService.findByUsername(messageText);
            long userIdToMakeUser = responseUserDto.getId();
            userService.makeAdmin(userIdToMakeUser);
            menuService.refreshCommands(userIdToMakeUser, Authority.ROLE_USER);
        } catch (EntityNotFoundException e) {
            return getSendMessage(chatId, format("Пользователь с ником %s не существует", messageText));
        }
        return getSendMessage(chatId, format("Пользователь %s теперь является пользователем", messageText));
    }
}
