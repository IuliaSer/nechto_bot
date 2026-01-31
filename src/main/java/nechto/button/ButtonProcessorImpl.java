package nechto.button;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

import static nechto.enums.Button.CALNOOP_BUTTON;
import static nechto.utils.BotUtils.getAnswerCallbackQuery;
import static nechto.utils.BotUtils.getButtonNameWithMessageId;

@Component
public class ButtonProcessorImpl implements ButtonProcessor {
    private final List<Button> buttonActions;
    private final ButtonService buttonService;

    Map<String, Button> buttonActionMap;

    public ButtonProcessorImpl(List<Button> buttonActions, ButtonService buttonService) {
        this.buttonActions = buttonActions;
        this.buttonService = buttonService;
        this.buttonActionMap = new ConcurrentHashMap<>();

        for (Button button : buttonActions) {
            buttonActionMap.put(button.getButton().name(), button);
        }
    }
//CALNAV_BUTTON:2025-03_8717321
//PICKED_BUTTON:132434_77674
    @Override
    public BotApiMethod<?> processButton(CallbackQuery callbackQuery, Long userId) {
        String buttonName = callbackQuery.getData();
        String buttonNameWithMessageId = getButtonNameWithMessageId(callbackQuery);
        if (CALNOOP_BUTTON.name().equals(buttonName) || !buttonService.isActive(buttonNameWithMessageId)) {
            return getAnswerCallbackQuery(callbackQuery);
        }

        return
                buttonActionMap.entrySet()
                        .stream()
                        .filter(e -> getBaseName(buttonName).equals(e.getKey()))
                        .map(Map.Entry::getValue)
                                .findFirst().orElseThrow(() -> new RuntimeException("Команда не найдена"))
                        .onButtonPressed(callbackQuery, userId);
    }

    private String getBaseName(String buttonName) {
        int idx = buttonName.indexOf(':');
        return idx == -1 ? buttonName : buttonName.substring(0, idx);
    }
}
