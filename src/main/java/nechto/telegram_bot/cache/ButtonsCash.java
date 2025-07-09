package nechto.telegram_bot.cache;

import lombok.Getter;
import lombok.Setter;
import nechto.telegram_bot.button.ButtonInfo;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Setter
public class ButtonsCash {
    private final Map<String, ButtonInfo> buttonMap = new ConcurrentHashMap<>();
}
