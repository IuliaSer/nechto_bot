package nechto.telegram_bot.botstate;

import nechto.enums.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotStateProcessorImpl implements BotStateProcessor {
    private final List<BotStateInterface> botStateInterfaces;

    private final Map<BotState, BotStateInterface> botStatesMap;

    public BotStateProcessorImpl(List<BotStateInterface> botStateInterfaces) {
        this.botStateInterfaces = botStateInterfaces;
        this.botStatesMap = new HashMap<>();

        for (BotStateInterface botStateInterface : botStateInterfaces) {
            botStatesMap.put(botStateInterface.getBotState(), botStateInterface);
        }
    }

    @Override
    public BotApiMethod<?> process(BotState botState, Message message) {
        return botStatesMap.get(botState).process(message);
    }
}
