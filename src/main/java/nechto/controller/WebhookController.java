package nechto.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nechto.service.TelegramWebhookRegistrator;
import nechto.telegram_bot.TelegramBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@Slf4j
@AllArgsConstructor
public class WebhookController {
    private final TelegramBot telegramBot;
    private final TelegramWebhookRegistrator telegramWebhookRegistrator;

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        log.debug("Inside webhook controller");
        if (Integer.parseInt(telegramWebhookRegistrator.getUpdatesAmount()) > 1) {
            telegramWebhookRegistrator.clearUpdates();
            telegramWebhookRegistrator.registerWebhook();
            log.debug("Updates cleared. Webhook registered again.");
            return null;
        }
        return telegramBot.onWebhookUpdateReceived(update);
    }

}
