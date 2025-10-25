package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.BotStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.CHANGE_GAME;
import static nechto.enums.BotState.START_CHANGE_GAME;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class StartChangeGame implements BotState {
    private final BotStateCache botStateCache;

    @Override
    public nechto.enums.BotState getBotState() {
        return START_CHANGE_GAME;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        botStateCache.saveBotState(userId, CHANGE_GAME);
        return getSendMessage(userId, "Введите ник игрока, для которого надо изменить очки");
    }

}
