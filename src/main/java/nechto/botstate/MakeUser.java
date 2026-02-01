package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.service.InlineKeyboardService;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.MAKE_USER;
import static nechto.utils.BotUtils.getSendMessage;

@Component
@RequiredArgsConstructor
public class MakeUser implements BotState {
    private final InlineKeyboardService inlineKeyboardService;
    private final UserService userService;

    @Override
    public nechto.enums.BotState getBotState() {
        return MAKE_USER;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        return getSendMessage(userId, "Выберите ник игрока, у которого надо забрать права админа:",
            inlineKeyboardService.returnButtonsWithAdmins(userService.findAllAdmins()));

    }
}
