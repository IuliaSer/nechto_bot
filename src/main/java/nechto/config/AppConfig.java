package nechto.config;

import lombok.RequiredArgsConstructor;
import nechto.service.MenuService;
import nechto.service.UserService;
import nechto.service.TelegramBot;
import nechto.service.TelegramFacade;
import nechto.cache.UserInfoCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@RequiredArgsConstructor
@Configuration
public class AppConfig {
    private final TelegramBotConfig botConfig;
    private final UserService userService;
    private final MenuService menuService;
    private final UserInfoCache userInfoCache;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botConfig.getWebHookPath()).build();
    }

    @Bean
    public TelegramBot springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
        TelegramBot bot = new TelegramBot(setWebhook, telegramFacade, userService, menuService, userInfoCache);
        bot.setBotToken(botConfig.getBotToken());
        bot.setBotUsername(botConfig.getUserName());
        bot.setBotPath(botConfig.getWebHookPath());
        return bot;
    }
}
