package nechto.telegram_bot.button.count;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.Button;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static java.lang.String.format;
import static nechto.enums.Button.PLUS_ANTI_HUMAN_BUTTON;

@RequiredArgsConstructor
@Component
public class PlusButtonAntiHuman implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresStateCache scoresStateCache;

    @Override
    public nechto.enums.Button getButton() {
        return PLUS_ANTI_HUMAN_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        RequestScoresDto requestScoresDto = scoresStateCache.get(userId);
        int messageId = callbackQuery.getMessage().getMessageId();
        int antiHumanFlamethrowerAmount = requestScoresDto.getAntiHumanFlamethrowerAmount();
        requestScoresDto.setAntiHumanFlamethrowerAmount(++antiHumanFlamethrowerAmount);

        return inlineKeyboardService.editeMessageForInlineKeyboardPlusMinusForAntiHuman(userId, messageId,
                format("Выберите количество:\n"), antiHumanFlamethrowerAmount);
    }
}
