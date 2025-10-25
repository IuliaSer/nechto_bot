package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.BotStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.MAKE_USER;
import static nechto.enums.BotState.MAKE_USER_START;
import static nechto.utils.BotUtils.getSendMessage;

@Component
@RequiredArgsConstructor
public class MakeUserStart implements BotState {
    private final BotStateCache botStateCache;

    @Override
    public nechto.enums.BotState getBotState() {
        return MAKE_USER_START;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        botStateCache.saveBotState(userId, MAKE_USER);
        return getSendMessage(userId, "Введите ник игрока, которого надо сделать пользователем:"); //или забрать права :)
    }
}
