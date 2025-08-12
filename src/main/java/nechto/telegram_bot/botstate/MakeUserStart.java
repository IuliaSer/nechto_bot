package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.enums.BotState;
import nechto.service.RoleService;
import nechto.telegram_bot.cache.BotStateCache;
import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.*;

@Component
@RequiredArgsConstructor
public class MakeUserStart implements BotStateInterface {
    private final BotStateCache botStateCache;
    private final RoleService roleService;

    @Override
    public BotState getBotState() {
        return MAKE_USER_START;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        roleService.isOwner(userId);
        botStateCache.saveBotState(userId, MAKE_USER);
        return BotUtils.getSendMessage(userId, "Введите ник игрока, которого надо сделать пользователем:"); //или забрать права :)
    }
}
