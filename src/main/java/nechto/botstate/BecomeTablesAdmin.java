package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.entity.Table;
import nechto.service.InlineKeyboardService;
import nechto.service.TableService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static nechto.enums.BotState.BECOME_TABLES_ADMIN;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class BecomeTablesAdmin implements BotState {
    private final InlineKeyboardService inlineKeyboardService;
    private final TableService tableService;

    @Override
    public nechto.enums.BotState getBotState() {
        return BECOME_TABLES_ADMIN;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long adminId = message.getFrom().getId();
        List<Table> tables = tableService.findByToday();

        return getSendMessage(adminId,
                "Выбери стол за которым вы хотите стать админом:",
                inlineKeyboardService.returnButtonsWithTables(tables));
    }
}
