package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.telegram_bot.cache.ButtonsCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@RequiredArgsConstructor
@Component
public class ButtonServiceImpl implements ButtonService {
    private final ButtonsCache buttonsCache;

    @Override
    public boolean isActive(String buttonName) {
        var buttonInfo = buttonsCache.getButtonMap().get(buttonName);

        if (buttonInfo != null && !buttonInfo.isActive()) {
            buttonInfo.getButton().setCallbackData("noop");
            return false;
        }
        return true;
    }

    @Override
    public void deactivateButtons(String... names) {
        for (String buttonToInactivate: names) {
            buttonsCache.getButtonMap().get(buttonToInactivate).setActive(false);
        }
    }

    @Override
    public void putButtonsToButtonCache(InlineKeyboardButton... inlineKeyboardButtons) {
        for (InlineKeyboardButton button: inlineKeyboardButtons) {
            buttonsCache.getButtonMap().put(button.getCallbackData(), new ButtonInfo(true, button));
        }
    }

    @Override
    public void activateAllButtons() {
        buttonsCache.getButtonMap().values().forEach(buttonInfo -> buttonInfo.setActive(true));
    }

    @Override
    public void deactivateAllButtons() {
        buttonsCache.getButtonMap().values().forEach(buttonInfo -> buttonInfo.setActive(false));
    }
}
