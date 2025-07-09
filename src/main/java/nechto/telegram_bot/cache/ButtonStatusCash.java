package nechto.telegram_bot.cache;

import lombok.Getter;
import lombok.Setter;
import nechto.enums.BotState;
import nechto.enums.Button;
import nechto.enums.Status;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Setter
public class ButtonStatusCash {
    private final Map<Button, Status> buttonStatusMap = new ConcurrentHashMap<>();

    ButtonStatusCash() {
        buttonStatusMap.put(Button.NECHTO_BUTTON, Status.NECHTO);
        buttonStatusMap.put(Button.HUMAN_BUTTON, Status.HUMAN);
        buttonStatusMap.put(Button.CONTAMINATED_BUTTON, Status.CONTAMINATED);
        buttonStatusMap.put(Button.NECHTO_BUTTON, Status.NECHTO);
        buttonStatusMap.put(Button.WIN_BUTTON, Status.WON);
        buttonStatusMap.put(Button.LOOSE_BUTTON, Status.LOOSE);
        buttonStatusMap.put(Button.DANGEROUS_BUTTON, Status.DANGEROUS);
        buttonStatusMap.put(Button.USEFULL_BUTTON, Status.USEFULL);
        buttonStatusMap.put(Button.BURNED_BUTTON, Status.BURNED);
        buttonStatusMap.put(Button.VICTIM_BUTTON, Status.VICTIM);
        buttonStatusMap.put(Button.FLAMETHROWER_BUTTON, Status.FLAMETHROWER);
        buttonStatusMap.put(Button.ANTI_HUMAN_FLAMETHROWER_BUTTON, Status.ANTI_HUMAN_FLAMETHROWER);
    }
}
