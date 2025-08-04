package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.enums.BotState;
import nechto.service.RoleService;
import nechto.telegram_bot.cache.BotStateCash;
import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.MAKE_ADMIN;
import static nechto.enums.BotState.MAKE_ADMIN_START;

@Component
@RequiredArgsConstructor
public class MakeAdminStart implements BotStateInterface {
    private final BotStateCash botStateCash;
    private final RoleService roleService;

    @Override
    public BotState getBotState() {
        return MAKE_ADMIN_START;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        roleService.isOwner(userId);
        botStateCash.saveBotState(userId, MAKE_ADMIN);
        return BotUtils.getSendMessage(userId, "Введите ник игрока, которого надо сделать админом:");
    }
}
