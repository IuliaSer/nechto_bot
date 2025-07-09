package nechto.telegram_bot.cache;

import lombok.Getter;
import lombok.Setter;
import nechto.dto.request.RequestScoresDto;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import static nechto.utils.CommonConstants.SCORES;

@Component
@Getter
@Setter
public class ScoresStateCash {
    private final Map<String, RequestScoresDto> scoresStateMap = new ConcurrentHashMap<>();
    private final RequestScoresDto requestScoresDto = new RequestScoresDto();

    public ScoresStateCash() {
        scoresStateMap.put(SCORES, requestScoresDto);
    }
}
