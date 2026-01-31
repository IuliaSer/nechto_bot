package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.cache.ButtonsCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.PICKED_BUTTON;
import static nechto.utils.BotUtils.getButtonNameWithMessageId;

@RequiredArgsConstructor
@Component
public class ButtonServiceImpl implements ButtonService {
    private final ButtonsCache buttonsCache;

    @Override
    public boolean isActive(String buttonName) {
        if (buttonsCache.get(buttonName) != null && !buttonsCache.get(buttonName)) {
            return false;
        }
        return true;
    }

    @Override
    public void deactivateButtons(String... names) {
        for (String buttonToInactivate: names) {
            buttonsCache.put(buttonToInactivate, false);
        }
    }

    @Override
    public void putButtonsToButtonCache(String... buttons) {
        for (String button: buttons) {
            buttonsCache.put(button, true);
        }
    }

    @Override
    public void deactivateAllPickedUserButtons(CallbackQuery callbackQuery) {
        buttonsCache.getMap().keySet().stream()
                .filter(b -> b.startsWith(PICKED_BUTTON.name()))
                .forEach(b -> buttonsCache.put(getButtonNameWithMessageId(callbackQuery, b), false));
    }
}
