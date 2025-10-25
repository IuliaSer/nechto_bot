package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.service.InlineKeyboardService;
import nechto.cache.BotStateCache;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.BotState.COUNT;
import static nechto.enums.Button.WIN_NECHTO_BUTTON;
import static nechto.enums.CommandStatus.NECHTO_WIN;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class NechtoWinStatusButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final ButtonService buttonService;
    private final BotStateCache botStateCache;

    @Override
    public nechto.enums.Button getButton() {
        return WIN_NECHTO_BUTTON;
    };

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().getName())) {
            return null;
        }
        scoresStateCache.get(userId).setCommandStatus(NECHTO_WIN);
        botStateCache.saveBotState(userId, COUNT);

        return getSendMessage(userId, "Введите ник игрока, которого надо посчитать:");
    }
}
