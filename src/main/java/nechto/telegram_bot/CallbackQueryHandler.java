package nechto.telegram_bot;

import lombok.RequiredArgsConstructor;
import nechto.telegram_bot.button.ButtonProcessor;
import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.utils.BotUtils.getSendMessage;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler {
    private final ButtonProcessor buttonProcessor;

    public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
        String buttonName = callbackQuery.getData();
        long userId = callbackQuery.getFrom().getId();

        try {
            return buttonProcessor.processButton(callbackQuery, buttonName, userId);
        } catch (Exception e) {
            return BotUtils.getSendMessage(userId, "Кнопка не найдена.");
        }
    }
}
