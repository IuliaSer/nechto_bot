package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.ScoresStateCache;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class StartCount implements BotState {
    private final ScoresStateCache scoresStateCache;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public nechto.enums.BotState getBotState() {
        return nechto.enums.BotState.START_COUNT;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        if (scoresStateCache.get(userId).isGameIsFinished()) {
            throw new RuntimeException("Создайте новую игру!");
        }
        return inlineKeyboardService.returnButtonsWithCommandStatuses(userId);
    }
}
