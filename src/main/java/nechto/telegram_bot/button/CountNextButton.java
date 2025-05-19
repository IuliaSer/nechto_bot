package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.service.RoleService;
import nechto.telegram_bot.BotStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.BotState.COUNT;
import static nechto.utils.BotUtils.getSendMessageWithInlineMarkup;
import static nechto.utils.CommonConstants.*;

@RequiredArgsConstructor
@Component
public class CountNextButton implements Button {
    private final BotStateCash botStateCash;
    private final RoleService roleService;

    @Override
    public String getButtonName() {
        return COUNT_NEXT_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        roleService.checkIsAdmin(userId);
        botStateCash.saveBotState(userId, COUNT);
        return getSendMessageWithInlineMarkup(userId, "Введите ник игрока, которого надо посчитать: ");
    }
}
