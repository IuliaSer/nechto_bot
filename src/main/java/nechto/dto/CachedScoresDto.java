package nechto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CachedScoresDto {
    private Long userId;

    private Long gameId;

    private int flamethrowerAmount;

    private int antiHumanFlamethrowerAmount;

//    @Builder.Default
    private boolean gameIsFinished = false;

}
