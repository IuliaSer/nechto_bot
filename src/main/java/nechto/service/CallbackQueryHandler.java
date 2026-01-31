package nechto.service;

import lombok.RequiredArgsConstructor;
import nechto.button.ButtonProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.utils.BotUtils.getSendMessage;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler {
    private final ButtonProcessor buttonProcessor;

    public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
        long userId = callbackQuery.getFrom().getId();

        try {
            return buttonProcessor.processButton(callbackQuery, userId);
        } catch (Exception e) {
            return getSendMessage(userId, e.getMessage());
        }
    }
}
