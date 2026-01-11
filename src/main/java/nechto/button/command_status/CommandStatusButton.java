package nechto.button.command_status;

import lombok.RequiredArgsConstructor;
import nechto.button.Button;
import nechto.button.ButtonService;
import nechto.cache.ScoresStateCache;
import nechto.dto.CachedScoresDto;
import nechto.service.InlineKeyboardService;
import nechto.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.utils.BotUtils.getButtonNameWithMessageId;
import static nechto.utils.BotUtils.getEditMessageWithInlineMarkup;

@Component
@RequiredArgsConstructor
abstract public class CommandStatusButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final ButtonService buttonService;
    private final InlineKeyboardService inlineKeyboardService;
    private final UserService userService;

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = getButtonNameWithMessageId(callbackQuery, getButton());
        int messageId = callbackQuery.getMessage().getMessageId();
        if (!buttonService.isActive(buttonName)) {
            return null;
        }

        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long gameId = cachedScoresDto.getGameId();
        if (cachedScoresDto.isGameIsFinished()) {
            return getEditMessageWithInlineMarkup(userId, messageId,
                    "Завершить изменения или изменить очки для игроков:",
                    inlineKeyboardService.returnButtonsWithEndChangingAndChangeNext(userService.findAllByGameId(gameId)));
        }
        buttonService.deactivateButtons(buttonName);
        return getEditMessageWithInlineMarkup(userId, messageId,
                "Выберите ник игрока, которого надо посчитать:",
                inlineKeyboardService.returnButtonsWithUsers(userService.findAllByGameId(gameId)));
    }
}
