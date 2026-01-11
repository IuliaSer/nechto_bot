package nechto.cache;

import lombok.Getter;
import nechto.enums.Button;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class ButtonsCache {
    private final Map<String, Boolean> map;

    public Boolean get(String buttonId) {
        return map.get(buttonId);
    }

    public void put(String buttonId, Boolean status) {
        map.put(buttonId, status);
    }

    public ButtonsCache() {
        this.map = new ConcurrentHashMap<>();
        for (Button button : Button.values()) {
            put(button.name(), true);
        }
    }
}
