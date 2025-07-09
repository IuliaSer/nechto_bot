package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.telegram_bot.cache.ButtonsCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@RequiredArgsConstructor
@Component
public class ButtonServiceImpl implements ButtonService {
    private final ButtonsCash buttonsCash;

    @Override
    public boolean isActive(String buttonName, String... names) {
        var buttonInfo = buttonsCash.getButtonMap().get(buttonName);

        if (buttonInfo != null && !buttonInfo.isActive()) {
            buttonInfo.getButton().setCallbackData("noop");
            return false;
        }

        for (String buttonToInactivate: names) {
            buttonsCash.getButtonMap().get(buttonToInactivate).setActive(false);
        }
        return true;
    }

    @Override
    public void putButtonsToButtonCache(InlineKeyboardButton... inlineKeyboardButtons) {
        for (InlineKeyboardButton button: inlineKeyboardButtons) {
            buttonsCash.getButtonMap().put(button.getCallbackData(), new ButtonInfo(true, button));
        }
    }

    @Override
    public void activateAllButtons() {
        buttonsCash.getButtonMap().values().forEach(buttonInfo -> buttonInfo.setActive(true));
    }
}
