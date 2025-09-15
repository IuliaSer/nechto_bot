package nechto.telegram_bot.cache;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ScoresStateCache {
    private final Map<Long, RequestScoresDto> map = new ConcurrentHashMap<>();

    public RequestScoresDto getOrCreate(long chatId) { //?
        return map.computeIfAbsent(chatId, id -> new RequestScoresDto());
    }

    public void put(long chatId) {
        map.put(chatId, new RequestScoresDto());
    }

    public RequestScoresDto get(long chatId) {
        return map.get(chatId);
    }
}
