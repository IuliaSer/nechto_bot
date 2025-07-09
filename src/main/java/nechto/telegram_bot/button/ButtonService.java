package nechto.telegram_bot.button;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface ButtonService {

    boolean isActive(String buttonName, String... names);

    void putButtonsToButtonCache(InlineKeyboardButton... inlineKeyboardButtons);

    void activateAllButtons();
}
