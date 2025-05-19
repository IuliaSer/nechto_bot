package nechto.telegram_bot.botstate;

import nechto.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotStateInterface {
    BotState getBotState();

    BotApiMethod<?> process(Message message);
}
