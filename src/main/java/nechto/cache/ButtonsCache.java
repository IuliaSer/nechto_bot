package nechto.cache;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class ButtonsCache {
    private final Map<String, Boolean> map = new ConcurrentHashMap<>();

    public Boolean get(String buttonId) {
        return map.get(buttonId);
    }

    public void put(String buttonId, Boolean status) {
        map.put(buttonId, status);
    }

}
