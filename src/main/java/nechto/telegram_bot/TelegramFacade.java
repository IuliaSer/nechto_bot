package nechto.telegram_bot;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import nechto.dto.response.ResponseUserDto;
import nechto.enums.Authority;
import nechto.enums.BotState;
import nechto.exception.RoleException;
import nechto.service.RoleService;
import nechto.service.UserService;
import nechto.telegram_bot.cache.BotStateCache;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

import static nechto.enums.BotState.*;

@Component
@RequiredArgsConstructor
public class TelegramFacade {

    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final BotStateCache botStateCache;
    private final RoleService roleService;
    private final UserService userService;
    private final ScoresStateCache scoresStateCache;

    public BotApiMethod<?> handleUpdate(Update update, long userId) throws IOException, WriterException {
        if (!scoresStateCache.getScoresStateMap().containsKey(userId)) {
            scoresStateCache.putToScoresStateCache(userId);
        }
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

    public BotApiMethod<?> handleInputMessage(Message message) throws IOException, WriterException {
        BotState botState;
        String messageText = message.getText();
        Long userId = message.getFrom().getId();
        ResponseUserDto responseUserDto = userService.findById(userId);
        Authority authority = responseUserDto.getAuthority();
        if ("/create_game".equals(messageText)) {
            if (authority.equals(Authority.ROLE_USER)) {
                throw new RoleException("Access denied. This action is not available to user");
            }
            botState = CREATE_GAME;
            botStateCache.saveBotState(userId, CREATE_GAME);
        } else if ("/register".equals(messageText)) {
            botState = START_REGISTRATION;
        } else if (messageText.startsWith("/start add")) {
            botState = ADD_USER;
        } else if ("/start_count".equals(messageText)) {
            if (authority.equals(Authority.ROLE_USER)) {
                throw new RoleException("Access denied. This action is not available to user");
            }
            botState = START_COUNT;
        } else if ("/show_results".equals(messageText)) {
            botState = SHOW_RESULTS;
        } else if ("/change_game".equals(messageText)) {
            if (authority.equals(Authority.ROLE_USER)) {
                throw new RoleException("Access denied. This action is not available to user");
            }
            botState = START_CHANGE_GAME;
        } else if ("/make_admin".equals(messageText)) {
            if (!authority.equals(Authority.ROLE_OWNER)) {
                throw new RoleException("Access denied. This action is available only to owner");
            }
            botState = MAKE_ADMIN_START;
        } else if ("/make_user".equals(messageText)) {
            if (!authority.equals(Authority.ROLE_OWNER)) {
                throw new RoleException("Access denied. This action is available only to owner");
            }
            botState = MAKE_USER_START;
        } else {
            botState = botStateCache.getBotStateMap().get(userId) == null ?
                    BotState.START : botStateCache.getBotStateMap().get(userId);
        }
        return messageHandler.handle(message, botState);
    }
}
