package nechto.telegram_bot.botstate;

import com.google.zxing.WriterException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        var state = botStatesMap.get(botState);
        if (state != null) {
            return state.process(message);
        } else {
            throw new RuntimeException("No corresponding bot state found");
        }
    }
}
