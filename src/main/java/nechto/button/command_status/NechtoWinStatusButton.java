package nechto.button.command_status;

import nechto.button.ButtonService;
import nechto.cache.ScoresStateCache;
import nechto.service.InlineKeyboardService;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.WIN_NECHTO_BUTTON;
import static nechto.enums.CommandStatus.NECHTO_WIN;
import static nechto.utils.BotUtils.getButtonNameWithMessageId;

@Component
public class NechtoWinStatusButton extends CommandStatusButton {
    private final ScoresStateCache scoresStateCache;
    private final ButtonService buttonService;

    public NechtoWinStatusButton(ScoresStateCache scoresStateCache, ButtonService buttonService, InlineKeyboardService inlineKeyboardService, UserService userService) {
        super(scoresStateCache, buttonService, inlineKeyboardService, userService);
        this.scoresStateCache = scoresStateCache;
        this.buttonService = buttonService;
    }

    @Override
    public nechto.enums.Button getButton() {
        return WIN_NECHTO_BUTTON;
    };

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = getButtonNameWithMessageId(callbackQuery, getButton());

        if (!buttonService.isActive(buttonName)) {
            return null;
        }
        scoresStateCache.get(userId).setCommandStatus(NECHTO_WIN);

        return super.onButtonPressed(callbackQuery, userId);
    }
}
