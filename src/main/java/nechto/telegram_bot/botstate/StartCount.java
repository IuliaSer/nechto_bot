package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.enums.BotState;
import nechto.service.RoleService;
import nechto.telegram_bot.BotStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.COUNT;
import static nechto.utils.BotUtils.getSendMessageWithInlineMarkup;

@Component
@RequiredArgsConstructor
public class StartCount implements BotStateInterface {
    private final BotStateCash botStateCash;
    private final RoleService roleService;

    @Override
    public BotState getBotState() {
        return BotState.START_COUNT;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        roleService.checkIsAdmin(userId);
        botStateCash.saveBotState(userId, COUNT);
        return getSendMessageWithInlineMarkup(userId, "Введите ник игрока, которого надо посчитать");
    }
}
