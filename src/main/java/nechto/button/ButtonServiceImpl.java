package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.cache.ButtonsCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@RequiredArgsConstructor
@Component
public class ButtonServiceImpl implements ButtonService {
    private final ButtonsCache buttonsCache;

    @Override
    public boolean isActive(String buttonName) {
        var buttonInfo = buttonsCache.get(buttonName);

        if (buttonInfo != null && !buttonInfo.isActive()) {
            buttonInfo.getButton().setCallbackData("noop");
            return false;
        }
        return true;
    }

    @Override
    public void deactivateButtons(String... names) {
        for (String buttonToInactivate: names) {
            buttonsCache.get(buttonToInactivate).setActive(false);
        }
    }

    @Override
    public void putButtonsToButtonCache(InlineKeyboardButton... inlineKeyboardButtons) {
        for (InlineKeyboardButton button: inlineKeyboardButtons) {
            buttonsCache.put(button.getCallbackData(), new ButtonInfo(true, button));
        }
    }

    @Override
    public void activateAllButtons() {
        buttonsCache.getValues().forEach(buttonInfo -> buttonInfo.setActive(true));
    }

    @Override
    public void deactivateAllButtons() {
        buttonsCache.getValues().forEach(buttonInfo -> buttonInfo.setActive(false));
    }
}
