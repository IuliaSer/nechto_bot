package nechto.button;

public interface ButtonService {

    boolean isActive(String buttonName);

    void deactivateButtons(String... names);

//    void deactivateAllButtons();

    void putButtonsToButtonCache(String... buttons);

    void deactivateAllPickedUserButtons();
}
