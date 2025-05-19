package nechto.status;

import nechto.entity.Scores;
import org.springframework.stereotype.Component;

import java.util.List;

import static nechto.enums.Status.CONTAMINATED;
import static nechto.enums.Status.WON;

@Component
public class Contaminated implements Status {

    @Override
    public float count(List<nechto.enums.Status> statuses, List<Scores> scoresList) {
        return statuses.contains(WON) ? 1 : -1;
    }

    @Override
    public nechto.enums.Status getStatus() {
        return CONTAMINATED;
    }
}
