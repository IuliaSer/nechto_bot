package nechto.telegram_bot;

import com.google.zxing.WriterException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import nechto.enums.Authority;
import nechto.service.MenuService;
import nechto.service.QrCodeGenerator;
import nechto.service.UserService;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.io.IOException;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBot extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;

    private TelegramFacade telegramFacade;
    private final UserService userService;
    private final MenuService menuService;
    private final QrCodeGenerator qrCodeGenerator;

    public TelegramBot(TelegramFacade telegramFacade, DefaultBotOptions options, SetWebhook setWebhook, UserService userService, MenuService menuService, QrCodeGenerator qrCodeGenerator) {
        super(options, setWebhook);
        this.telegramFacade = telegramFacade;
        this.userService = userService;
        this.menuService = menuService;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    public TelegramBot(SetWebhook setWebhook, TelegramFacade telegramFacade, UserService userService, MenuService menuService, QrCodeGenerator qrCodeGenerator) {
        super(setWebhook);
        this.telegramFacade = telegramFacade;
        this.userService = userService;
        this.menuService = menuService;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        long userId;
        if (update.hasMessage()) {
            userId = update.getMessage().getFrom().getId();
        } else {
            userId = update.getCallbackQuery().getFrom().getId();
        }
        Authority authority = userService.findById(userId).getAuthority();

        menuService.refreshCommands(userId, authority);
        try {
            return telegramFacade.handleUpdate(update, userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

}
