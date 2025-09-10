package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.telegram_bot.cache.BotStateCache;
import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.MAKE_ADMIN;
import static nechto.enums.BotState.MAKE_ADMIN_START;

@Component
@RequiredArgsConstructor
public class MakeAdminStart implements BotState {
    private final BotStateCache botStateCache;

    @Override
    public nechto.enums.BotState getBotState() {
        return MAKE_ADMIN_START;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        botStateCache.saveBotState(userId, MAKE_ADMIN);
        return BotUtils.getSendMessage(userId, "Введите ник игрока, которого надо сделать админом:");
    }
}
