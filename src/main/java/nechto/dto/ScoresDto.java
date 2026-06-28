package nechto.dto;

import lombok.Data;
import nechto.enums.Status;

import java.util.List;

@Data
public class ScoresDto {
    private Long id;

    private UserDto user;

    private GameDto game;

    private float scores;

    private List<Status> statuses;
}
