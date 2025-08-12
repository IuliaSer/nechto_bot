package nechto.telegram_bot.botstate;

import com.google.zxing.WriterException;
import nechto.enums.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BotStateProcessorImpl implements BotStateProcessor {
    private final List<BotStateInterface> botStateInterfaces;

    private final Map<BotState, BotStateInterface> botStatesMap;

    public BotStateProcessorImpl(List<BotStateInterface> botStateInterfaces) {
        this.botStateInterfaces = botStateInterfaces;
        this.botStatesMap = new ConcurrentHashMap<>();

        for (BotStateInterface botStateInterface : botStateInterfaces) {
            botStatesMap.put(botStateInterface.getBotState(), botStateInterface);
        }
    }

    @Override
    public BotApiMethod<?> process(BotState botState, Message message) throws IOException, WriterException {
        return botStatesMap.get(botState).process(message);
    }
}
