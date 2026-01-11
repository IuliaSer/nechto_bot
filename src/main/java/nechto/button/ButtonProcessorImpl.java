package nechto.button;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ButtonProcessorImpl implements ButtonProcessor {
    private final List<Button> buttonActions;

    Map<String, Button> buttonActionMap;

    public ButtonProcessorImpl(List<Button> buttonActions) {
        this.buttonActions = buttonActions;
        this.buttonActionMap = new ConcurrentHashMap<>();

        for (Button button : buttonActions) {
            buttonActionMap.put(button.getButton().name(), button);
        }
    }

    @Override
    public BotApiMethod<?> processButton(CallbackQuery callbackQuery, String buttonName, Long userId) {
            return
                    buttonActionMap.entrySet()
                            .stream()
                            .filter(e -> (buttonName).equals(e.getKey()) ||
                                    (isCalendarOrPickedButton(buttonName) && buttonName.startsWith(e.getKey()))) //plohoe reshenie
                            .map(Map.Entry::getValue)
                                    .findFirst().orElseThrow(() -> new RuntimeException("Команда не найдена"))
                            .onButtonPressed(callbackQuery, userId);
    }

    private boolean isCalendarOrPickedButton(String buttonName) {
        return buttonName.startsWith("CAL") || buttonName.startsWith("PICKED");
    }
}
