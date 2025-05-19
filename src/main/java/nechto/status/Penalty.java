package nechto.status;

import nechto.entity.Scores;
import org.springframework.stereotype.Component;

import java.util.List;

import static nechto.enums.Status.PENALTY;

@Component
public class Penalty implements Status {

    @Override
    public float count(List<nechto.enums.Status> statuses, List<Scores> scoresList) {
        return -2;
    }

    @Override
    public nechto.enums.Status getStatus() {
        return PENALTY;
    }
}
