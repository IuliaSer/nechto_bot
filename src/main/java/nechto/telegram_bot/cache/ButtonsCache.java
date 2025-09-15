package nechto.telegram_bot.cache;

import lombok.Getter;
import nechto.telegram_bot.button.ButtonInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class ButtonsCache {
    private final Map<String, ButtonInfo> map = new ConcurrentHashMap<>();

    public ButtonInfo get(String buttonId) {
        return map.get(buttonId);
    }

    public void put(String buttonId, ButtonInfo buttonInfo) {
        map.put(buttonId, buttonInfo);
    }

    public Collection<ButtonInfo> getValues() {
        return map.values();
    }
}
