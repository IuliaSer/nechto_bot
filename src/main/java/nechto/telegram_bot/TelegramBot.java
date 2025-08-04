package nechto.telegram_bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import nechto.enums.Authority;
import nechto.service.MenuService;
import nechto.service.UserService;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.util.List;

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

    public TelegramBot(TelegramFacade telegramFacade, DefaultBotOptions options, SetWebhook setWebhook, UserService userService, MenuService menuService) {
        super(options, setWebhook);
        this.telegramFacade = telegramFacade;
        this.userService = userService;
        this.menuService = menuService;
    }

    public TelegramBot(SetWebhook setWebhook, TelegramFacade telegramFacade, UserService userService, MenuService menuService) {
        super(setWebhook);
        this.telegramFacade = telegramFacade;
        this.userService = userService;
        this.menuService = menuService;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        long userId = update.getMessage().getFrom().getId();
        Authority authority = userService.findById(userId).getAuthority();

        menuService.refreshCommands(userId, authority);

        return telegramFacade.handleUpdate(update);
    }

    public void setCommands(List<BotCommand> commands) throws TelegramApiException {
        this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
    }

}
