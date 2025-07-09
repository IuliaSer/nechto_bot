package nechto.telegram_bot.button;

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
        if (buttonActionMap.containsKey(buttonName)) {
            return buttonActionMap.get(buttonName).onButtonPressed(callbackQuery, userId);
        } else {
            throw new RuntimeException("Unknown button pressed");
        }
    }
}
