package nechto.telegram_bot;

import lombok.RequiredArgsConstructor;
import nechto.enums.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static nechto.enums.BotState.ADD_USER;
import static nechto.enums.BotState.MAKE_ADMIN_START;
import static nechto.enums.BotState.START_COUNT;
import static nechto.enums.BotState.START_REGISTRATION;

@Component
@RequiredArgsConstructor
public class TelegramFacade {

    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final BotStateCash botStateCash;

    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else {
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                return handleInputMessage(update.getMessage());
            }
        }
        return null;
    }

    public BotApiMethod<?> handleInputMessage(Message message) {
        BotState botState;
        String messageText = message.getText();

        if ("/create_game".equals(messageText)) {
            botState = BotState.CREATE_GAME;
        } else if ("/register".equals(messageText)) {
            botState = START_REGISTRATION;
        } else if (messageText.startsWith("/start add")) {
            botState = ADD_USER;
        } else if ("/start_count".equals(messageText)) {
            botState = START_COUNT;
        } else if ("/make_admin".equals(messageText)) {
            botState = MAKE_ADMIN_START;
        } else {
            botState = botStateCash.getBotStateMap().get(message.getFrom().getId()) == null ?
                    BotState.START : botStateCash.getBotStateMap().get(message.getFrom().getId());
        }
        return messageHandler.handle(message, botState);
    }
}
