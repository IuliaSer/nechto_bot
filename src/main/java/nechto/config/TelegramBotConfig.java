package nechto.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class TelegramBotConfig {
    @Value("${telegram.bot.webHookPath}")
    String webHookPath;

    @Value("${telegram.bot.userName}")
    String userName;

    @Value("${telegram.bot.token}")
    String botToken;
}
