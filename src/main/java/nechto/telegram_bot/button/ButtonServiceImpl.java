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
    public boolean isActive(String buttonName, String... names) {
        var buttonInfo = buttonsCache.getButtonMap().get(buttonName);

        if (buttonInfo != null && !buttonInfo.isActive()) {
            buttonInfo.getButton().setCallbackData("noop");
            return false;
        }

        for (String buttonToInactivate: names) {
            buttonsCache.getButtonMap().get(buttonToInactivate).setActive(false);
        }
        return true;
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
}
