package nechto.telegram_bot.button.attributes;

import nechto.enums.Status;
import nechto.service.ScoresService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.VICTIM_BUTTON;

@Component
public class VictimButton extends AttributeButton {

    public VictimButton(ScoresStateCash scoresStateCash, ScoresService scoresService, ButtonService buttonService) {
        super(scoresStateCash, scoresService, buttonService);
    }

    @Override
    public String getButtonName() {
        return VICTIM_BUTTON.name();
    }

    @Override
    public Status getStatus() {
        return Status.VICTIM;
    }

}
