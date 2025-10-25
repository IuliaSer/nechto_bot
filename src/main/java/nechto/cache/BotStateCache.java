package nechto.cache;

import nechto.enums.BotState;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Component
public class BotStateCache {
    private final Map<Long, BotState> map = new ConcurrentHashMap<>();

    public void saveBotState(long userId, BotState botState) {
        map.put(userId, botState);
    }

    public BotState get(long userId) {
        return map.get(userId);
    }

    public BotState getOrDefault(long userId, BotState defaultState) {
        return map.getOrDefault(userId, defaultState);
    }

    public BotState getOrCreate(long userId, Supplier<BotState> supplier) {
        return map.computeIfAbsent(userId, id -> supplier.get());
    }
}
