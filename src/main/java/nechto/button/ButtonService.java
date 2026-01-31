package nechto.button;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface ButtonService {

    boolean isActive(String buttonName);

    void deactivateButtons(String... names);

//    void deactivateAllButtons();

    void putButtonsToButtonCache(String... buttons);

    void deactivateAllPickedUserButtons(CallbackQuery callbackQuery);
}
