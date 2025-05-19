package nechto.telegram_bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import nechto.service.RoleService;
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
    private final RoleService roleService;

    public TelegramBot(TelegramFacade telegramFacade, DefaultBotOptions options, SetWebhook setWebhook, RoleService roleService) {
        super(options, setWebhook);
        this.telegramFacade = telegramFacade;
        this.roleService = roleService;
    }

    public TelegramBot(SetWebhook setWebhook, TelegramFacade telegramFacade, RoleService roleService) {
        super(setWebhook);
        this.telegramFacade = telegramFacade;
        this.roleService = roleService;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
//        long userId = update.getMessage().getFrom().getId();
//
//        List<BotCommand> adminCommands = List.of(
//                new BotCommand("/create_game", "создать игру"),
//                new BotCommand("/start_count", "посчитать очки конкретного игрока")
//        );
//        List<BotCommand> userCommands = List.of(new BotCommand("/register", "зарегистрироваться"));
//        if (roleService.checkIsAdmin(userId) || roleService.checkIsOwner(userId)) {
//            try {
//                setCommands(adminCommands);
//            } catch (TelegramApiException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            try {
//                setCommands(userCommands);
//            } catch (TelegramApiException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
        return telegramFacade.handleUpdate(update);
    }

    public void setCommands(List<BotCommand> commands) throws TelegramApiException {
        this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
    }

}
