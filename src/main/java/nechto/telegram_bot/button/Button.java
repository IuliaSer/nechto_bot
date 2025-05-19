package nechto.telegram_bot.button;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface Button {
    String getButtonName();
    BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId);
}
