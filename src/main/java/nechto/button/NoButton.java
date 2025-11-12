package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.NO_BUTTON;

@RequiredArgsConstructor
@Component
public class NoButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public nechto.enums.Button getButton() {
        return NO_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {

        return inlineKeyboardService.returnButtonsForContaminated(userId);
    }
}
