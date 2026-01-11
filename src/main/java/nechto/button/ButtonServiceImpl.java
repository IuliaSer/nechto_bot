package nechto.button;

import lombok.RequiredArgsConstructor;
import nechto.cache.ButtonsCache;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.PICKED_BUTTON;

@RequiredArgsConstructor
@Component
public class ButtonServiceImpl implements ButtonService {
    private final ButtonsCache buttonsCache;

    @Override
    public boolean isActive(String buttonName) {
        return buttonsCache.get(buttonName);

//        if (buttonInfo != null && !buttonInfo.isActive()) {
//            buttonInfo.getButton().setCallbackData("noop");
//            return false;
//        }
//        return true;
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

//    @Override
//    public void deactivateAllButtons() {
//        buttonsCache.getValues().forEach(buttonInfo -> buttonInfo.setActive(false));
//    }

    @Override
    public void deactivateAllPickedUserButtons() {
        buttonsCache.getMap().keySet().stream()
                .filter(b -> b.startsWith(PICKED_BUTTON.name()))
                .forEach(b -> buttonsCache.put(b, false));
    }
}
