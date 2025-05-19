package nechto.telegram_bot;

import lombok.Getter;
import lombok.Setter;
import nechto.dto.request.RequestScoresDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static nechto.utils.CommonConstants.SCORES;

@Component
@Getter
@Setter
public class ScoresStateCash {
    private final Map<String, RequestScoresDto> scoresStateMap = new HashMap<>();
    private final RequestScoresDto requestScoresDto = new RequestScoresDto();

    public ScoresStateCash() {
        scoresStateMap.put(SCORES, requestScoresDto);
    }
}
