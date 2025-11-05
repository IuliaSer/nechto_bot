package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.BotStateCache;
import nechto.cache.ScoresStateCache;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.CHANGE_GAME;
import static nechto.enums.BotState.START_CHANGE_GAME;
import static nechto.utils.BotUtils.getEditMessageWithInlineMarkup;

@RequiredArgsConstructor
@Component
public class StartChangeGame implements BotState {
    private final BotStateCache botStateCache;
    private final ScoresStateCache scoresStateCache;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public nechto.enums.BotState getBotState() {
        return START_CHANGE_GAME;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        scoresStateCache.get(userId); //proverka na to chto igra zaregistrirovana
        botStateCache.saveBotState(userId, CHANGE_GAME);
        return getEditMessageWithInlineMarkup(userId, message.getMessageId(),
                "Выберите ник игрока, для которого надо изменить очки:",
                inlineKeyboardService.returnButtonsWithUsers(userId, scoresStateCache.get(userId).getGameId()));
    }

}
