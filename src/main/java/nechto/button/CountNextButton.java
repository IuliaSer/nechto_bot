package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.cache.BotStateCache;
import nechto.cache.ScoresStateCache;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.BotState.COUNT;
import static nechto.enums.Button.COUNT_NEXT_BUTTON;
import static nechto.utils.BotUtils.getEditMessageWithInlineMarkup;

@RequiredArgsConstructor
@Component
public class CountNextButton implements Button {
    private final BotStateCache botStateCache;
    private final ButtonService buttonService;
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;

    @Override
    public nechto.enums.Button getButton() {
        return COUNT_NEXT_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        botStateCache.saveBotState(userId, COUNT);
        buttonService.deactivateAllButtons(); // нужно ли? ведь были конкретные кнопки
        return getEditMessageWithInlineMarkup(userId, callbackquery.getMessage().getMessageId(),
                "Выберите ник игрока, которого надо посчитать:",
                inlineKeyboardService.returnButtonsWithUsers(userId, scoresStateCache.get(userId).getGameId()));
    }
}
