package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.telegram_bot.cache.BotStateCache;
import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.COUNT;

@Component
@RequiredArgsConstructor
public class StartCount implements BotState {
    private final BotStateCache botStateCache;

    @Override
    public nechto.enums.BotState getBotState() {
        return nechto.enums.BotState.START_COUNT;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        botStateCache.saveBotState(userId, COUNT);
        return BotUtils.getSendMessage(userId, "Введите ник игрока, которого надо посчитать");
    }
}
