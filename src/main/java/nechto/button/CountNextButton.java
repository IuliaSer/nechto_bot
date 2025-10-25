package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.cache.BotStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.BotState.COUNT;
import static nechto.enums.Button.COUNT_NEXT_BUTTON;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class CountNextButton implements Button {
    private final BotStateCache botStateCache;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return COUNT_NEXT_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        botStateCache.saveBotState(userId, COUNT);
        buttonService.deactivateAllButtons(); // нужно ли? ведь были конкретные кнопки
        return getSendMessage(userId, "Введите ник игрока, которого надо посчитать: ");
    }
}
