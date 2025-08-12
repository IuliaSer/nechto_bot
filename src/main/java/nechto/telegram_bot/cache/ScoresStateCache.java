package nechto.telegram_bot.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nechto.dto.request.RequestScoresDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class ScoresStateCache {
    private final Map<Long, RequestScoresDto> scoresStateMap = new ConcurrentHashMap<>();
    private final RequestScoresDto requestScoresDto = new RequestScoresDto();

    public void putToScoresStateCache(Long chatId) {
        scoresStateMap.put(chatId, requestScoresDto);
    }
}
