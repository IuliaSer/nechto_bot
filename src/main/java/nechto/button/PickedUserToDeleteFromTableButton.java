package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.cache.TableAdminCache;
import nechto.entity.Table;
import nechto.entity.User;
import nechto.service.TableService;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.PICKED_USER_BUTTON_TO_DELETE;

@RequiredArgsConstructor
@Component
public class PickedUserToDeleteFromTableButton implements Button {
    private final UserService userService;
    private final TableAdminCache tableAdminCache;
    private final TableService tableService;

    @Override
    public nechto.enums.Button getButton() {
        return PICKED_USER_BUTTON_TO_DELETE;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = callbackQuery.getData();

        long userIdToDelete = Long.parseLong(buttonName.substring(PICKED_USER_BUTTON_TO_DELETE.name().length() + 1));
        User user = userService.findById(userIdToDelete);
        Long tableId = tableAdminCache.get(userId);
        Table table = tableService.findById(tableId);
        table.getCurrentUsers().remove(user);

        tableService.save(table);

        return null;
    }

}
