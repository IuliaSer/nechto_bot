package nechto.status;

import nechto.entity.Scores;

import java.util.List;

public interface Status {

    float count(List<nechto.enums.Status> statuses, List<Scores> scoresList);

    nechto.enums.Status getStatus();
}
