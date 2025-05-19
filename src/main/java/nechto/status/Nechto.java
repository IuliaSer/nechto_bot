package nechto.status;

import nechto.entity.Scores;
import org.springframework.stereotype.Component;

import java.util.List;

import static nechto.enums.Status.CONTAMINATED;
import static nechto.enums.Status.NECHTO;
import static nechto.enums.Status.WON;

@Component
public class Nechto implements Status {

    @Override
    public float count(List<nechto.enums.Status> statuses, List<Scores> scoresList) {
        float scores = 0;

        if (statuses.contains(WON)) {
            scores += 2;
        } else {
            scores -= 1;
            for (Scores score : scoresList) {
                if (score.getStatuses().contains(CONTAMINATED)) {
                    scores -= 0.3;
                }
            }
        }
        return scores > 2 ? 2 : scores;
    }

    @Override
    public nechto.enums.Status getStatus() {
        return NECHTO;
    }
}
