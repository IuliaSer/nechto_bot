package nechto.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nechto.dto.response.ResponseUserDto;
import nechto.enums.Authority;
import nechto.enums.BotCommand;
import nechto.enums.BotState;
import nechto.exception.RoleException;
import nechto.cache.BotStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static nechto.enums.Authority.ROLE_USER;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramFacade {

    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final UserService userService;
    private final BotStateCache botStateCache;

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

    public BotApiMethod<?> handleInputMessage(Message message) { //вынести в messageHandler
        long userId = message.getFrom().getId();
        Optional<ResponseUserDto> responseUserDto = userService.findById(userId);
        Authority authority = responseUserDto.isPresent()  ? responseUserDto.get().getAuthority() : ROLE_USER; //вынести метод в utils
        BotState botState = BotCommand.match(message.getText())
                    .map(cmd -> {
                        if (!cmd.isAllowed(authority)) {
                            throw new RoleException("Доступ запрещен для роли " + authority.getName());
                        }
                        return cmd.state();
                    })
                    .orElseGet(() -> {
                        BotState cached = botStateCache.get(userId);
                        return cached != null ? cached : BotState.START; //why start?
                    });
        return messageHandler.handle(message, botState);
    }
}
