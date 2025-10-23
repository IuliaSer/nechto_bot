package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.telegram_bot.cache.BotStateCache;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.COUNT;
import static nechto.utils.BotUtils.getSendMessage;

@Component
@RequiredArgsConstructor
public class StartCount implements BotState {
    private final BotStateCache botStateCache;
    private final ScoresStateCache scoresStateCache;

    @Override
    public nechto.enums.BotState getBotState() {
        return nechto.enums.BotState.START_COUNT;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        if (scoresStateCache.get(userId).isGameIsFinished()) {
            return getSendMessage(userId, "Создайте новую игру!");
        }
        botStateCache.saveBotState(userId, COUNT);
        return getSendMessage(userId, "Введите ник игрока, которого надо посчитать:");
    }
}
