package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.TableAdminCache;
import nechto.entity.Table;
import nechto.service.TableService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static java.lang.String.format;
import static nechto.enums.BotState.GIVE_NAME_TO_TABLE;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class GiveNameToTable implements BotState {
    private final TableService tableService;
    private final TableAdminCache tableAdminCache;

    @Override
    public nechto.enums.BotState getBotState() {
        return GIVE_NAME_TO_TABLE;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long adminId = message.getFrom().getId();
        String tableName = message.getText();

        boolean tableNameExist = tableService.findByToday()
                .stream()
                .anyMatch(t -> t.getName().equals(tableName));
        if (tableNameExist) {
            throw new RuntimeException(format("Стол с названием %s уже существует" ));
        }
        long tableId = tableAdminCache.get(adminId);
        Table table = tableService.findById(tableId);
        table.setName(tableName);
        tableService.save(table);

        return getSendMessage(adminId, format("Теперь вы %s!", tableName));
    }
}
