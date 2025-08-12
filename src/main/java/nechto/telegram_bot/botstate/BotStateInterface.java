package nechto.telegram_bot.botstate;

import com.google.zxing.WriterException;
import nechto.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;

public interface BotStateInterface {
    BotState getBotState();

    BotApiMethod<?> process(Message message) throws IOException, WriterException;
}
