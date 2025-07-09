package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.service.RoleService;
import nechto.telegram_bot.cache.BotStateCash;
import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.BotState.COUNT;
import static nechto.enums.Button.COUNT_NEXT_BUTTON;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class CountNextButton implements Button {
    private final BotStateCash botStateCash;
    private final RoleService roleService;
    private final ButtonService buttonService;

    @Override
    public String getButtonName() {
        return COUNT_NEXT_BUTTON.name();
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        roleService.checkIsAdmin(userId);
        botStateCash.saveBotState(userId, COUNT);
        buttonService.activateAllButtons();
        return BotUtils.getSendMessage(userId, "Введите ник игрока, которого надо посчитать: ");
    }
}
