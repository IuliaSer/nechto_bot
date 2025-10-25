package nechto.button.attributes;

import nechto.enums.Button;
import nechto.enums.Status;
import nechto.service.ScoresService;
import nechto.button.ButtonService;
import nechto.cache.ScoresStateCache;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.DANGEROUS_BUTTON;

@Component
public class DangerousButton extends AttributeButton {

    public DangerousButton(ScoresStateCache scoresStateCache, ScoresService scoresService, ButtonService buttonService) {
        super(scoresStateCache, scoresService, buttonService);
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
