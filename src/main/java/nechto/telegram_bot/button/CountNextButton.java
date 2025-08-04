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

@RequiredArgsConstructor
@Component
public class CountNextButton implements Button {
    private final BotStateCash botStateCash;
    private final RoleService roleService;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return COUNT_NEXT_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        roleService.isAdmin(userId);
        botStateCash.saveBotState(userId, COUNT);
        buttonService.activateAllButtons();
        return BotUtils.getSendMessage(userId, "Введите ник игрока, которого надо посчитать: ");
    }
}
