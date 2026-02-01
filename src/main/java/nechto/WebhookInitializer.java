package nechto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nechto.service.TelegramWebhookRegistrator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookInitializer {
    private final TelegramWebhookRegistrator telegramWebhookRegistrator;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        telegramWebhookRegistrator.clearUpdates();
        telegramWebhookRegistrator.registerWebhook();
        log.info("Webhook registered on application startup");
    }
}

