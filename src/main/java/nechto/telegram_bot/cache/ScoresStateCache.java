package nechto.telegram_bot.cache;

import nechto.dto.CachedScoresDto;
import nechto.dto.request.RequestScoresDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ScoresStateCache {
    private final Map<Long, CachedScoresDto> map = new ConcurrentHashMap<>();

    public CachedScoresDto getOrCreate(long userId) {
        return map.computeIfAbsent(userId, id -> new CachedScoresDto());
    }

    public void put(long userId) {
        map.put(userId, new CachedScoresDto());
    }

    public CachedScoresDto get(long userId) {
        return map.get(userId);
    }
}
