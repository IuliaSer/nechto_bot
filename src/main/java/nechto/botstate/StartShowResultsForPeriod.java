package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.BotStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.SHOW_RESULTS_FOR_A_PERIOD;
import static nechto.enums.BotState.START_SHOW_RESULTS_FOR_A_PERIOD;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class StartShowResultsForPeriod implements BotState {
    private final BotStateCache botStateCache;

    @Override
    public nechto.enums.BotState getBotState() {
        return START_SHOW_RESULTS_FOR_A_PERIOD;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        botStateCache.saveBotState(userId, SHOW_RESULTS_FOR_A_PERIOD);
        return getSendMessage(userId, "Введите даты через ';' с какого по какое посмотреть рейтинг. " +
                "В формате: 2025,10,26; 2025,11,27");
    }
}
