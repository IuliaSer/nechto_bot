package nechto.telegram_bot;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import nechto.enums.BotState;
import nechto.telegram_bot.botstate.BotStateProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final BotStateProcessor botStateProcessor;

    public BotApiMethod<?> handle(Message message, BotState botState) throws IOException, WriterException {
        return botStateProcessor.process(botState, message);
    }

}
