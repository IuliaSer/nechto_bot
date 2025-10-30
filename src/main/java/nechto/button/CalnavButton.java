package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.time.YearMonth;
import java.util.Locale;

import static nechto.enums.Button.CALNAV_BUTTON;
import static nechto.utils.BotUtils.getEditMessageWithInlineMarkup;

@RequiredArgsConstructor
@Component
public class CalnavButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public nechto.enums.Button getButton() {
        return CALNAV_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String message = callbackQuery.getData();
        YearMonth target = YearMonth.parse(message.substring(CALNAV_BUTTON.name().length() + 1));
        return getEditMessageWithInlineMarkup(userId, callbackQuery.getMessage().getMessageId(), "Привяу",
                inlineKeyboardService.buildCalendar(userId, target, Locale.forLanguageTag("ru")));
    }
}
