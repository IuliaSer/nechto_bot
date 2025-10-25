package nechto.button;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface ButtonService {

    boolean isActive(String buttonName);

    void deactivateButtons(String... names);

    void putButtonsToButtonCache(InlineKeyboardButton... inlineKeyboardButtons);

    void activateAllButtons();

    void deactivateAllButtons();
}
