package nechto.telegram_bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nechto.enums.Authority;
import nechto.service.MenuService;
import nechto.service.QrCodeGenerator;
import nechto.service.UserService;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Getter
@Setter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBot extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;

    private TelegramFacade telegramFacade;
    private final UserService userService;
    private final MenuService menuService;
    private final QrCodeGenerator qrCodeGenerator;

    public TelegramBot(SetWebhook setWebhook, TelegramFacade telegramFacade, UserService userService,
                       MenuService menuService, QrCodeGenerator qrCodeGenerator) {
        super(setWebhook);
        this.telegramFacade = telegramFacade;
        this.userService = userService;
        this.menuService = menuService;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Long userId;
        Authority authority;
        if (update.hasMessage()) {
            userId = update.getMessage().getFrom().getId();
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getFrom() != null) {
            userId = update.getCallbackQuery().getFrom().getId();
        } else {
            log.debug("Unsupported update type: {}", update);
            return null;
        }
        authority = userService.findById(userId).getAuthority();
        menuService.refreshCommands(userId, authority);
        return telegramFacade.handleUpdate(update, userId);
    }

}
