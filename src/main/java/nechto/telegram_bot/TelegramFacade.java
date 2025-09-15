package nechto.telegram_bot;

import lombok.RequiredArgsConstructor;
import nechto.dto.response.ResponseUserDto;
import nechto.enums.Authority;
import nechto.enums.BotCommand;
import nechto.enums.BotState;
import nechto.exception.RoleException;
import nechto.service.UserService;
import nechto.telegram_bot.cache.BotStateCache;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static nechto.utils.BotUtils.getSendMessage;

@Component
@RequiredArgsConstructor
public class TelegramFacade {

    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final UserService userService;
    private final ScoresStateCache scoresStateCache;
    private final BotStateCache botStateCache;

    public BotApiMethod<?> handleUpdate(Update update, long userId) {
        scoresStateCache.put(userId);

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
        String messageText = message.getText();
        Long userId = message.getFrom().getId();
        ResponseUserDto responseUserDto = userService.findById(userId);
        Authority authority = responseUserDto.getAuthority();
        BotState botState;
        try {
            botState = BotCommand.match(messageText)
                    .map(cmd -> {
                        if (!cmd.isAllowed(authority)) {
                            throw new RoleException("Access denied for " + authority.getName());
                        }
                        return cmd.state();
                    })
                    .orElseGet(() -> {
                        BotState cached = botStateCache.get(userId);
                        return cached != null ? cached : BotState.START; //why start?
                    });
        } catch (RoleException e) {
            return getSendMessage(userId, "Доступ запрещен для роли " + authority.getName());
        }
        return messageHandler.handle(message, botState);
    }
}
