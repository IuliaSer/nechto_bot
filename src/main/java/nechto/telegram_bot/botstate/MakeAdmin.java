package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.dto.response.ResponseUserDto;
import nechto.enums.BotState;
import nechto.service.RoleService;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;
import static nechto.enums.BotState.MAKE_ADMIN;
import static nechto.utils.BotUtils.getSendMessageWithInlineMarkup;

@Component
@RequiredArgsConstructor
public class MakeAdmin implements BotStateInterface {
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public BotState getBotState() {
        return MAKE_ADMIN;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        final Long userId = message.getChatId();
        String messageText = message.getText();
        try {
            roleService.checkIsOwner(userId);
            ResponseUserDto responseUserDto = userService.findByUsername(messageText);
            userService.makeAdmin(responseUserDto.getId());
        } catch (EntityNotFoundException e) {
            return getSendMessageWithInlineMarkup(userId, format("Пользователь с ником %s не существует", messageText));
        }
        return getSendMessageWithInlineMarkup(userId, format("Пользователь %s теперь является админом", messageText));
    }
}
