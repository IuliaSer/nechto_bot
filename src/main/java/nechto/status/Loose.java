package nechto.status;

import nechto.entity.Scores;
import org.springframework.stereotype.Component;

import java.util.List;

import static nechto.enums.Status.LOOSE;

@Component
public class Loose implements Status {

    @Override
    public float count(List<nechto.enums.Status> statuses, List<Scores> scoresList) {
        return 0;
    }

    @Override
    public nechto.enums.Status getStatus() {
        return LOOSE;
    }
}
