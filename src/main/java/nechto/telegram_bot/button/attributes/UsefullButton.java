package nechto.telegram_bot.button.attributes;

import nechto.enums.Button;
import nechto.enums.Status;
import nechto.service.ScoresService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.USEFULL_BUTTON;

@Component
public class UsefullButton extends AttributeButton {

    public UsefullButton(ScoresStateCache scoresStateCache, ScoresService scoresService, ButtonService buttonService) {
        super(scoresStateCache, scoresService, buttonService);
    }

    @Override
    public Button getButton() {
        return USEFULL_BUTTON;
    }

    @Override
    public Status getStatus() {
        return Status.USEFULL;
    }

}
