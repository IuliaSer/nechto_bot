package nechto.button.attributes;

import nechto.enums.Button;
import nechto.enums.Status;
import nechto.service.ScoresService;
import nechto.button.ButtonService;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.VICTIM_BUTTON;

@Component
public class VictimButton extends AttributeButton {

    public VictimButton(ScoresStateCache scoresStateCache, ScoresService scoresService, ButtonService buttonService) {
        super(scoresStateCache, scoresService, buttonService);
    }

    @Override
    public Button getButton() {
        return VICTIM_BUTTON;
    }

    @Override
    public Status getStatus() {
        return Status.VICTIM;
    }

}
