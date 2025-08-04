package nechto.config;

import lombok.RequiredArgsConstructor;
import nechto.service.MenuService;
import nechto.service.UserService;
import nechto.telegram_bot.TelegramBot;
import nechto.telegram_bot.TelegramFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@RequiredArgsConstructor
@Configuration
public class AppConfig {
    private final TelegramBotConfig botConfig;
    private final UserService userService;
    private final MenuService menuService;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botConfig.getWebHookPath()).build();
    }

    @Bean
    public TelegramBot springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
        TelegramBot bot = new TelegramBot(setWebhook, telegramFacade, userService, menuService);
        bot.setBotToken(botConfig.getBotToken());
        bot.setBotUsername(botConfig.getUserName());
        bot.setBotPath(botConfig.getWebHookPath());

//        List<BotCommand> commands = List.of(
//                new BotCommand("/register", "зарегистрироваться"),
//                new BotCommand("/create_game", "создать игру"),
//                new BotCommand("/start_count", "посчитать очки конкретного игрока"),
//                new BotCommand("/show_results", "показать результаты"),
//                new BotCommand("/change_game", "изменить игру")
//        );
//
//        bot.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));

        return bot;
    }
}
