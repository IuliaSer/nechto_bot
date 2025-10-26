package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.dto.CachedScoresDto;
import nechto.enums.CommandStatus;
import nechto.service.GameService;
import nechto.service.UserService;
import nechto.service.InlineKeyboardService;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.COUNT;

@RequiredArgsConstructor
@Component
public class Count implements BotState {
    private final GameService gameService;
    private final UserService userService;
    private final ScoresStateCache scoresStateCache;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public nechto.enums.BotState getBotState() {
        return COUNT;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        String userName = message.getText();
        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long userIdToCount = userService.findByUsernameOrThrow(userName).getId();
        long gameId = scoresStateCache.get(userId).getGameId();

        gameService.addUser(gameId, userIdToCount);

        cachedScoresDto.setUserId(userIdToCount);

        return scoresStateCache.get(userId).getCommandStatus().equals(CommandStatus.NECHTO_WIN) ?
            inlineKeyboardService.returnButtonsWithRolesForNechtoWin(userId) :
            inlineKeyboardService.returnButtonsWithRolesForHumanWin(userId);
    }
}
