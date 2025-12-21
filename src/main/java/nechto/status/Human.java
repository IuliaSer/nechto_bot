package nechto.status;

import nechto.entity.Scores;
import org.springframework.stereotype.Component;

import java.util.List;

import static nechto.enums.Status.BURNED;
import static nechto.enums.Status.CONTAMINATED;
import static nechto.enums.Status.HUMAN;
import static nechto.enums.Status.LOOSE;
import static nechto.enums.Status.WON;

@Component
public class Human implements Status {
    private static final int NECHTO = 1;

    @Override
    public float count(List<nechto.enums.Status> statuses, List<Scores> scoresByGame) {
        float scores = 0;
        int amountOfWinners = 0;
        int amountOfContaminated = 0;
        if (statuses.contains(BURNED) || statuses.contains(LOOSE)) {
            return scores;
        }
        scores += 1;
        for (Scores score : scoresByGame) {
            List<nechto.enums.Status> statusList = score.getStatuses();
            if (statusList.contains(WON)) {
                amountOfWinners++;
            } else if (statusList.contains(CONTAMINATED)) {
                amountOfContaminated++;
            }
        }
        int diffBetweenContaminatedAndWinners = (NECHTO + amountOfContaminated) - amountOfWinners;
        if (diffBetweenContaminatedAndWinners > 0) {
            scores += diffBetweenContaminatedAndWinners;
        }
        return scores;
    }

    @Override
    public nechto.enums.Status getStatus() {
        return HUMAN;
    }
}
