package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.BotStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.GIVE_NAME_TO_TABLE;
import static nechto.enums.BotState.GIVE_NAME_TO_TABLE_START;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class GiveNameToTableStart implements BotState {
    private final BotStateCache botStateCache;

    @Override
    public nechto.enums.BotState getBotState() {
        return GIVE_NAME_TO_TABLE_START;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long adminId = message.getFrom().getId();

        botStateCache.saveBotState(adminId, GIVE_NAME_TO_TABLE);
        return getSendMessage(adminId, "Введите название для вашего стола");
    }
}
