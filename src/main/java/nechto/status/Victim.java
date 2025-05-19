package nechto.status;

import nechto.entity.Scores;
import org.springframework.stereotype.Component;

import java.util.List;

import static nechto.enums.Status.VICTIM;

@Component
public class Victim implements Status {

    @Override
    public float count(List<nechto.enums.Status> statuses, List<Scores> scoresList) {
        return 0.5f;
    }

    @Override
    public nechto.enums.Status getStatus() {
        return VICTIM;
    }
}
