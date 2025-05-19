package nechto.telegram_bot.button;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface ButtonProcessor {
    BotApiMethod<?> processButton(CallbackQuery callbackQuery, String buttonName, Long userId);
}
