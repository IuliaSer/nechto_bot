package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.telegram_bot.cache.BotStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.REGISTRATION;
import static nechto.enums.BotState.START_REGISTRATION;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class StartRegistration implements BotState {
    private final BotStateCache botStateCache;

    @Override
    public nechto.enums.BotState getBotState() {
        return START_REGISTRATION;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        botStateCache.saveBotState(userId, REGISTRATION);
        return getSendMessage(userId, "Введите своё имя и ник, например: Иван ivan123");
    }
}
