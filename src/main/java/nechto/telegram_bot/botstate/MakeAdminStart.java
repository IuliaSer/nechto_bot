package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.enums.BotState;
import nechto.service.RoleService;
import nechto.telegram_bot.BotStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.MAKE_ADMIN;
import static nechto.enums.BotState.MAKE_ADMIN_START;
import static nechto.utils.BotUtils.getSendMessageWithInlineMarkup;

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
        roleService.checkIsOwner(userId);
        botStateCash.saveBotState(userId, MAKE_ADMIN);
        return getSendMessageWithInlineMarkup(userId, "Введите ник игрока, которого надо сделать админом:");
    }
}
