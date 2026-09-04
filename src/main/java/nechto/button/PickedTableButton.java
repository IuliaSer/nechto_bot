package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.cache.TableAdminCache;
import nechto.entity.Table;
import nechto.service.TableService;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static java.lang.String.format;
import static nechto.enums.Button.PICKED_TABLE_BUTTON;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class PickedTableButton implements Button {
    private final TableAdminCache tableAdminCache;
    private final TableService tableService;
    private final UserService userService;

    @Override
    public nechto.enums.Button getButton() {
        return PICKED_TABLE_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long adminId) {
        String buttonName = callbackQuery.getData();
        long tableId = Long.parseLong(buttonName.substring(PICKED_TABLE_BUTTON.name().length() + 1));  //vinesti v otdel method
        Table table = tableService.findById(tableId);

        long oldAdminId = table.getAdmin().getId();
        tableAdminCache.getMap().remove(oldAdminId); //vinesti v otdel method
        tableAdminCache.saveAdminTable(adminId, tableId);

        table.setAdmin(userService.findById(adminId));
        tableService.save(table);

        return getSendMessage(adminId, format("Ты теперь админ за столом: %s", table.getName()));
    }

}
