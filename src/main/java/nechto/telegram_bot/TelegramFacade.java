package nechto.telegram_bot;

import lombok.RequiredArgsConstructor;
import nechto.enums.BotState;
import nechto.telegram_bot.cache.BotStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static nechto.enums.BotState.ADD_USER;
import static nechto.enums.BotState.CHANGE_GAME;
import static nechto.enums.BotState.CREATE_GAME;
import static nechto.enums.BotState.MAKE_ADMIN;
import static nechto.enums.BotState.MAKE_ADMIN_START;
import static nechto.enums.BotState.SHOW_RESULTS;
import static nechto.enums.BotState.START;
import static nechto.enums.BotState.START_CHANGE_GAME;
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
        Long userId = message.getFrom().getId();

        if ("/create_game".equals(messageText)) {
            botState = CREATE_GAME;
            botStateCash.saveBotState(userId, CREATE_GAME);
        } else if ("/register".equals(messageText)) {
            botState = START_REGISTRATION;
        } else if (messageText.startsWith("/start add")) {
            botState = ADD_USER;
        } else if ("/start_count".equals(messageText)) {
            botState = START_COUNT;
        } else if ("/show_results".equals(messageText)) {
            botState = SHOW_RESULTS;
        } else if ("/change_game".equals(messageText)) {
            botState = START_CHANGE_GAME;
        } else if ("/make_admin".equals(messageText)) {
            botState = MAKE_ADMIN_START;
        } else {
            botState = botStateCash.getBotStateMap().get(userId) == null ?
                    BotState.START : botStateCash.getBotStateMap().get(userId);
        }
        return messageHandler.handle(message, botState);
    }
}
