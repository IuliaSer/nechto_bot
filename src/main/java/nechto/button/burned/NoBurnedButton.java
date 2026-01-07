package nechto.button.burned;

import lombok.RequiredArgsConstructor;
import nechto.button.Button;
import nechto.button.ButtonService;
import nechto.cache.ScoresStateCache;
import nechto.enums.Status;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.NO_BURNED_BUTTON;
import static nechto.enums.Button.YES_BURNED_BUTTON;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class NoBurnedButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ButtonService buttonService;
    private final ScoresStateCache scoresStateCache;

    @Override
    public nechto.enums.Button getButton() {
        return NO_BURNED_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        Status status = scoresStateCache.get(userId).getStatus();
        if(!buttonService.isActive(getButton().name())) {
            return null;
        }
        buttonService.deactivateButtons(getButton().name(), YES_BURNED_BUTTON.name());
        if (status.equals(Status.HUMAN)) {
            return inlineKeyboardService.returnButtonsForHuman(userId);
        } else if (status.equals(Status.CONTAMINATED)) {
            return inlineKeyboardService.returnButtonsForContaminated(userId);
        } else {
            return getSendMessage(userId, "Не выбран статус человек, зараженный или нечто!");
        }
    }
}
