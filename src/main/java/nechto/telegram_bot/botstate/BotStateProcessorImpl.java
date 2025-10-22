package nechto.telegram_bot.botstate;

import nechto.exception.UnsupportedUpdateType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static nechto.utils.BotUtils.getSendMessage;

@Component
public class BotStateProcessorImpl implements BotStateProcessor {
    private final List<BotState> botStates;

    private final Map<nechto.enums.BotState, BotState> botStatesMap;

    public BotStateProcessorImpl(List<BotState> botStates) {
        this.botStates = botStates;
        this.botStatesMap = new ConcurrentHashMap<>();

        for (BotState botState : botStates) {
            botStatesMap.put(botState.getBotState(), botState);
        }
    }

    @Override
    public BotApiMethod<?> process(nechto.enums.BotState botState, Message message) {
        try {
            var state = botStatesMap.get(botState);
            if (state != null) {
                return state.process(message);
            } else {
                throw new UnsupportedUpdateType("Нет такой команды");
            }
        } catch(Exception e) {
            return getSendMessage(message.getFrom().getId(), e.getMessage());
        }
    }
}
