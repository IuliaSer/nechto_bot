package nechto.telegram_bot.cache;

import lombok.Getter;
import lombok.Setter;
import nechto.enums.BotState;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
@Getter
@Setter
public class BotStateCache {
    private final Map<Long, BotState> botStateMap = new ConcurrentHashMap<>();

    public void saveBotState(long userId, BotState botState) {
        botStateMap.put(userId, botState);
    }
}
