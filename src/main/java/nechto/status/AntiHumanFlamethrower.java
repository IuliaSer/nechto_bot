package nechto.status;

import nechto.entity.Scores;
import org.springframework.stereotype.Component;

import java.util.List;

import static nechto.enums.Status.ANTI_HUMAN_FLAMETHROWER;

@Component
public class AntiHumanFlamethrower implements Status {

    @Override
    public float count(List<nechto.enums.Status> statuses, List<Scores> scoresList) {
        return -1f;
    }

    @Override
    public nechto.enums.Status getStatus() {
        return ANTI_HUMAN_FLAMETHROWER;
    }
}
