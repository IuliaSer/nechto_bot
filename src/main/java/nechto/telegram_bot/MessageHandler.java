package nechto.telegram_bot;

import lombok.RequiredArgsConstructor;
import nechto.enums.BotState;
import nechto.telegram_bot.botstate.BotStateProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final BotStateProcessor botStateProcessor;

    public BotApiMethod<?> handle(Message message, BotState botState) {
        return botStateProcessor.process(botState, message);
    }

}
