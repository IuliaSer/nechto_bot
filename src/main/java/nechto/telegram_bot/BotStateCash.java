package nechto.telegram_bot;

import lombok.Getter;
import lombok.Setter;
import nechto.enums.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
public class BotStateCash {
    private final Map<Long, BotState> botStateMap = new HashMap<>();

    public void saveBotState(long userId, BotState botState) {
        botStateMap.put(userId, botState);
    }
}
