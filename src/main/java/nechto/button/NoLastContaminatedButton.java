package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.NO_LAST_CONTAMINATED_BUTTON;
import static nechto.utils.BotUtils.getButtonNameWithMessageId;

@RequiredArgsConstructor
@Component
public class NoLastContaminatedButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return NO_LAST_CONTAMINATED_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = getButtonNameWithMessageId(callbackQuery, getButton());

        buttonService.deactivateButtons(buttonName);
        return inlineKeyboardService.returnButtonsForContaminated(userId);
    }
}
