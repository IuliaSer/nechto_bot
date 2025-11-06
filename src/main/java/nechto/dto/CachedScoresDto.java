package nechto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nechto.dto.response.ResponseUserDto;
import nechto.enums.CommandStatus;

import java.util.List;

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

    private CommandStatus commandStatus;

    private List<ResponseUserDto> users;
}
