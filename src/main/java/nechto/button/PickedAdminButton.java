package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.enums.Authority;
import nechto.service.MenuService;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static java.lang.String.format;
import static nechto.enums.Button.PICKED_ADMIN_BUTTON;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class PickedAdminButton implements Button {
    private final ButtonService buttonService;
    private final UserService userService;
    private final MenuService menuService;

    @Override
    public nechto.enums.Button getButton() {
        return PICKED_ADMIN_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = callbackQuery.getData();
        long userIdToMakeUser = Long.parseLong(buttonName.substring(PICKED_ADMIN_BUTTON.name().length() + 1));
        String userName = userService.findById(userIdToMakeUser).get().getUsername();
        userService.makeUser(userIdToMakeUser);
        menuService.refreshCommands(userIdToMakeUser, Authority.ROLE_USER);
        buttonService.deactivateAllPickedUserButtons(callbackQuery);
        return getSendMessage(userId, format("У пользователя %s теперь отсутствуют права админа", userName));
    }

}
