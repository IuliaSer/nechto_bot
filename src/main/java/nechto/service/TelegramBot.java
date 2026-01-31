package nechto.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nechto.dto.response.ResponseUserDto;
import nechto.enums.Authority;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.util.Optional;

import static nechto.utils.BotUtils.extractUserId;
import static nechto.utils.BotUtils.getSendMessage;

@Getter
@Setter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBot extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;

    final TelegramFacade telegramFacade;
    final UserService userService;
    final MenuService menuService;

    public TelegramBot(SetWebhook setWebhook, TelegramFacade telegramFacade, UserService userService,
                       MenuService menuService) {
        super(setWebhook);
        this.telegramFacade = telegramFacade;
        this.userService = userService;
        this.menuService = menuService;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Long userId = null;
        try {
            userId = extractUserId(update);
            Optional<ResponseUserDto> responseUserDto = userService.findById(userId);

            if (responseUserDto.isEmpty()) {
                menuService.refreshCommands(userId, Authority.ROLE_USER);
            }
            return telegramFacade.handleUpdate(update);
        } catch (Exception e) {
            return getSendMessage(userId, e.getMessage());
        }
    }

}
