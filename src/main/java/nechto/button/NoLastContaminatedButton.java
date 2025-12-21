package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.NO_LAST_CONTAMINATED_BUTTON;

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
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().name())) {
            return null;
        }
        buttonService.deactivateButtons(getButton().name());
        return inlineKeyboardService.returnButtonsForContaminated(userId);
    }
}
