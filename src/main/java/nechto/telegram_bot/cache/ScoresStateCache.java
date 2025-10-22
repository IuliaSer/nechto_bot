package nechto.telegram_bot.cache;

import nechto.dto.request.RequestScoresDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ScoresStateCache {
    private final Map<Long, RequestScoresDto> map = new ConcurrentHashMap<>();

    public RequestScoresDto getOrCreate(long userId) {
        return map.computeIfAbsent(userId, id -> new RequestScoresDto());
    }

    public void put(long userId) {
        map.put(userId, new RequestScoresDto());
    }

    public RequestScoresDto get(long userId) {
        return map.get(userId);
    }
}
