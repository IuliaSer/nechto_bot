package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestUserDto;
import nechto.enums.BotState;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.utils.BotUtils.getSendMessageWithInlineMarkup;

@Component
@RequiredArgsConstructor
public class Registration implements BotStateInterface {
    private final UserService userService;

    @Override
    public BotState getBotState() {
        return BotState.REGISTRATION;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        String messageText = message.getText();
        String[] parts = messageText.trim().split("\\s+");

        if (parts.length < 2) {
            return getSendMessageWithInlineMarkup(userId, "Неверный формат. Введите: имя ник");
        }

        String name = parts[0];
        String nickname = parts[1];
        RequestUserDto user = new RequestUserDto(userId, name, nickname, null, null);
        userService.save(user);

        return getSendMessageWithInlineMarkup(userId, "Вы успешно зарегистрированы как " + name + " (" + nickname + ")");
    }
}
