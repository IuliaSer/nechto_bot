package nechto.controller;

import lombok.AllArgsConstructor;
import nechto.telegram_bot.TelegramBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@AllArgsConstructor
public class WebhookController {
    private final TelegramBot telegramBot;

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
//        if (update.getMessage().getDate() < (System.currentTimeMillis() / 1000 - 60)) { //  если callback to nullpointer
//            // Старое сообщение, игнорируем
//            return null;
//        }
        return telegramBot.onWebhookUpdateReceived(update);
    }

}
