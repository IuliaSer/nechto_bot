package nechto.status;

import nechto.entity.Scores;
import org.springframework.stereotype.Component;

import java.util.List;

import static nechto.enums.Status.LAST_CONTAMINATED_LOOSE;

@Component
public class LastContaminatedLoose implements Status {

    @Override
    public float count(List<nechto.enums.Status> statuses, List<Scores> scoresList) {
        return -1;
    }

    @Override
    public nechto.enums.Status getStatus() {
        return LAST_CONTAMINATED_LOOSE;
    }
}
