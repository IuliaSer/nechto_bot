package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.enums.BotState;
import nechto.service.RoleService;
import nechto.telegram_bot.cache.BotStateCache;
import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.CHANGE_GAME;
import static nechto.enums.BotState.START_CHANGE_GAME;

@RequiredArgsConstructor
@Component
public class StartChangeGame implements BotStateInterface {
    private final RoleService roleService;
    private final BotStateCache botStateCache;

    @Override
    public BotState getBotState() {
        return START_CHANGE_GAME;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        roleService.isAdmin(userId);
        botStateCache.saveBotState(userId, CHANGE_GAME);
        return BotUtils.getSendMessage(userId, "Введите ник игрока, которого надо посчитать");
    }

}
