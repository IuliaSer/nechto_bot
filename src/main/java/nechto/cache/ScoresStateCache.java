package nechto.cache;

import nechto.dto.CachedScoresDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//создается в момент создания игры, т.е для для каждой игры свой
//dannie za poslednyy scores
//userId is adminId
@Component
public class ScoresStateCache {
    private final Map<Long, CachedScoresDto> map = new ConcurrentHashMap<>();

    public void put(long userId) {
        map.put(userId, new CachedScoresDto());
    }

    public CachedScoresDto get(long userId) {
        CachedScoresDto cachedScoresDto = map.get(userId);
        if (cachedScoresDto == null) {
            throw new RuntimeException("Игра не зарегестрирована. Создайте игру!");
        }
        return cachedScoresDto;
    }
}
