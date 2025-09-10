package nechto.telegram_bot.botstate;

import com.google.zxing.WriterException;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;

public interface BotState {
    nechto.enums.BotState getBotState();

    BotApiMethod<?> process(Message message);
}
