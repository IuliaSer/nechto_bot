package nechto.telegram_bot.button.attributes;

import nechto.enums.Button;
import nechto.enums.Status;
import nechto.service.ScoresService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.DANGEROUS_BUTTON;

@Component
public class DangerousButton extends AttributeButton {

    public DangerousButton(ScoresStateCash scoresStateCash, ScoresService scoresService, ButtonService buttonService) {
        super(scoresStateCash, scoresService, buttonService);
    }

    @Override
    public Button getButton() {
        return DANGEROUS_BUTTON;
    }

    @Override
    public Status getStatus() {
        return Status.DANGEROUS;
    }

}
