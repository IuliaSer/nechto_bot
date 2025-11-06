package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.cache.BotStateCache;
import nechto.cache.ScoresStateCache;
import nechto.service.InlineKeyboardService;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.WIN_NECHTO_BUTTON;
import static nechto.enums.CommandStatus.NECHTO_WIN;
import static nechto.utils.BotUtils.getEditMessageWithInlineMarkup;

@RequiredArgsConstructor
@Component
public class NechtoWinStatusButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final ButtonService buttonService;
    private final BotStateCache botStateCache;
    private final InlineKeyboardService inlineKeyboardService;
    private final UserService userService;

    @Override
    public nechto.enums.Button getButton() {
        return WIN_NECHTO_BUTTON;
    };

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().name())) {
            return null;
        }
        scoresStateCache.get(userId).setCommandStatus(NECHTO_WIN);

        return getEditMessageWithInlineMarkup(userId, callbackquery.getMessage().getMessageId(),
                "Выберите ник игрока, которого надо посчитать:",
                inlineKeyboardService.returnButtonsWithUsers(userService.findAllByGameId(scoresStateCache.get(userId).getGameId())));
    }
}
