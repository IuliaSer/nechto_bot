package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestUserDto;
import nechto.exception.EntityAlreadyExistsException;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.utils.BotUtils.getSendMessage;

@Component
@RequiredArgsConstructor
public class Registration implements BotState {
    private final UserService userService;

    @Override
    public nechto.enums.BotState getBotState() {
        return nechto.enums.BotState.REGISTRATION;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        String messageText = message.getText();
        String[] parts = messageText.trim().split("\\s+");

        if (parts.length != 2) {
            return getSendMessage(userId, "Неверный формат. Введите: имя ник");
        }

        String name = parts[0];
        String nickname = parts[1];
        RequestUserDto user = new RequestUserDto(userId, name, nickname, null);
        try {
            userService.save(user);
        } catch (EntityAlreadyExistsException e) {
            return getSendMessage(userId, "Пользователь с таким ником уже существует");
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return getSendMessage(userId, String.format("Не удалось сохранить пользователя! %s",
                    e.getMessage().substring(e.getMessage().indexOf(": ") + 2)));
        }

        return getSendMessage(userId, "Вы успешно зарегистрированы как " + name + " (" + nickname + ")");
    }
}
