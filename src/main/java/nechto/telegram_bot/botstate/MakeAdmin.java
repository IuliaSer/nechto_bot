package nechto.telegram_bot.botstate;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nechto.dto.response.ResponseUserDto;
import nechto.enums.Authority;
import nechto.enums.BotState;
import nechto.service.MenuService;
import nechto.service.RoleService;
import nechto.service.UserService;
import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static java.lang.String.format;
import static nechto.enums.BotState.MAKE_ADMIN;
import static nechto.utils.BotUtils.getSendMessage;

@Component
@RequiredArgsConstructor
public class MakeAdmin implements BotStateInterface {
    private final UserService userService;
    private final RoleService roleService;
    private final MenuService menuService;

    @Override
    public BotState getBotState() {
        return MAKE_ADMIN;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        final Long chatId = message.getChatId();
        String messageText = message.getText();
        try {
            roleService.isOwner(chatId);
            ResponseUserDto responseUserDto = userService.findByUsername(messageText);
            long userIdToMakeAdmin = responseUserDto.getId();
            userService.makeAdmin(userIdToMakeAdmin);
            menuService.refreshCommands(userIdToMakeAdmin, Authority.ROLE_ADMIN);
        } catch (EntityNotFoundException e) {
            return BotUtils.getSendMessage(chatId, format("Пользователь с ником %s не существует", messageText));
        }
        return BotUtils.getSendMessage(chatId, format("Пользователь %s теперь является админом", messageText));
    }
}
