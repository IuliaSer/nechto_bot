package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.telegram_bot.cache.BotStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.BotState.CHANGE_GAME;
import static nechto.enums.Button.CHANGE_NEXT_BUTTON;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class ChangeNextButton implements Button {
    private final BotStateCache botStateCache;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return CHANGE_NEXT_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        botStateCache.saveBotState(userId, CHANGE_GAME);
        buttonService.deactivateAllButtons(); // нужно ли? ведь были конкретные кнопки
        return getSendMessage(userId, "Введите ник игрока, для которого изменить очки: ");
    }
}
