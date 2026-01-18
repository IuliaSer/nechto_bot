package nechto.cache;

import nechto.enums.Button;
import nechto.enums.Status;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static nechto.enums.Status.*;

@Component
public class ButtonStatusCache {
    private final Map<Button, Status> buttonStatusMap = new ConcurrentHashMap<>();

    ButtonStatusCache() {
        buttonStatusMap.put(Button.NECHTO_BUTTON, NECHTO);
        buttonStatusMap.put(Button.HUMAN_BUTTON, HUMAN);
        buttonStatusMap.put(Button.CONTAMINATED_BUTTON, CONTAMINATED);
        buttonStatusMap.put(Button.LAST_CONTAMINATED_BUTTON, LAST_CONTAMINATED);
        buttonStatusMap.put(Button.DANGEROUS_BUTTON, DANGEROUS);
        buttonStatusMap.put(Button.USEFULL_BUTTON, USEFULL);
        buttonStatusMap.put(Button.BURNED_BUTTON, BURNED);
        buttonStatusMap.put(Button.VICTIM_BUTTON, VICTIM);
        buttonStatusMap.put(Button.FLAMETHROWER_BUTTON, FLAMETHROWER);
        buttonStatusMap.put(Button.AGAINST_HUMAN_FLAMETHROWER_BUTTON, ANTI_HUMAN_FLAMETHROWER);
    }

    public Status getStatus(Button button) {
        return buttonStatusMap.get(button);
    }
}
