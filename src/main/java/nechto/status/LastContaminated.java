package nechto.status;

import nechto.entity.Scores;
import org.springframework.stereotype.Component;

import java.util.List;

import static nechto.enums.Status.LAST_CONTAMINATED;

@Component
public class LastContaminated implements Status {

    @Override
    public float count(List<nechto.enums.Status> statuses, List<Scores> scoresList) {
        return 0;
    }

    @Override
    public nechto.enums.Status getStatus() {
        return LAST_CONTAMINATED;
    }
}
