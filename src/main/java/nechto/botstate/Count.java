package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.ScoresStateCache;
import nechto.dto.CachedScoresDto;
import nechto.service.InlineKeyboardService;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class Count implements BotState {
    private final ScoresStateCache scoresStateCache;
    private final InlineKeyboardService inlineKeyboardService;
    private final UserService userService;

    @Override
    public nechto.enums.BotState getBotState() {
        return nechto.enums.BotState.COUNT;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        if (cachedScoresDto.isGameIsFinished()) {
            throw new RuntimeException("Создайте новую игру!");
        }
        cachedScoresDto.setUsers(userService.findAllByGameId(cachedScoresDto.getGameId()));

        return inlineKeyboardService.returnButtonsWithCommandStatuses(userId);
    }
}
