package nechto.telegram_bot.botstate;

import com.google.zxing.WriterException;
import nechto.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;

public interface BotStateProcessor {
    BotApiMethod<?> process(BotState botState, Message message);
}
