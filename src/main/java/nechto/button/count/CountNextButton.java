package nechto.button.count;

import lombok.RequiredArgsConstructor;
import nechto.button.Button;
import nechto.button.ButtonService;
import nechto.cache.ScoresStateCache;
import nechto.dto.response.ResponseUserDto;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

import static nechto.enums.Button.COUNT_NEXT_BUTTON;
import static nechto.enums.Button.END_GAME_BUTTON;
import static nechto.utils.BotUtils.getButtonNameWithMessageId;
import static nechto.utils.BotUtils.getEditMessageWithInlineMarkup;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class CountNextButton implements Button {
    private final ButtonService buttonService;
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;

    @Override
    public nechto.enums.Button getButton() {
        return COUNT_NEXT_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = getButtonNameWithMessageId(callbackQuery, getButton());
        String endGameButtonName = getButtonNameWithMessageId(callbackQuery, END_GAME_BUTTON);

        List<ResponseUserDto> users = scoresStateCache.get(userId).getUsers();
        buttonService.deactivateButtons(buttonName, endGameButtonName);

        if (users.isEmpty()) {
            return getSendMessage(userId, "Все пользователи добавленные в игру посчитаны!");
        }
        return getEditMessageWithInlineMarkup(userId,
                callbackQuery.getMessage().getMessageId(),
                "Выберите ник игрока, которого надо посчитать:",
                inlineKeyboardService.returnButtonsWithUsers(users));
    }
}
