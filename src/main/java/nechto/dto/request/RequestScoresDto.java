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
//    @NotNull(message = "User id should not be null")
    private Long userId;

//    @NotNull(message = "Game id should not be null")
    private Long gameId;

//    @NotNull(message = "Statuses should not be null")
    private List<Status> statuses;

    private int flamethrowerAmount;
}
