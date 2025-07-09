package nechto.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nechto.enums.Status;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestScoresDto {
    private Long userId;

    private Long gameId;

    private List<Status> statuses;

    private int flamethrowerAmount;

    private int antiHumanFlamethrowerAmount;

}
