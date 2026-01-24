package nechto.botstate;

import nechto.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotStateProcessor {
    BotApiMethod<?> process(BotState botState, Message message);
}
