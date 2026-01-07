package nechto.button.command_status;

import nechto.button.ButtonService;
import nechto.cache.ScoresStateCache;
import nechto.service.InlineKeyboardService;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.CommandStatus.PEOPLE_WIN;

@Component
public class PeopleWinStatusButton extends CommandStatusButton {
    private final ScoresStateCache scoresStateCache;
    private final ButtonService buttonService;

    public PeopleWinStatusButton(ScoresStateCache scoresStateCache, ButtonService buttonService,
                                 InlineKeyboardService inlineKeyboardService, UserService userService) {
        super(scoresStateCache, buttonService, inlineKeyboardService, userService);
        this.scoresStateCache = scoresStateCache;
        this.buttonService = buttonService;
    }

    @Override
    public nechto.enums.Button getButton() {
        return nechto.enums.Button.WIN_PEOPLE_BUTTON;
    };

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().name())) {
            return null;
        }
        scoresStateCache.get(userId).setCommandStatus(PEOPLE_WIN);
        return super.onButtonPressed(callbackquery, userId);
    }
}
