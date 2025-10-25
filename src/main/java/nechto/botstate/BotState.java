package nechto.botstate;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotState {
    nechto.enums.BotState getBotState();

    BotApiMethod<?> process(Message message);
}
