package nechto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AggregateScoresDto {
    private String username;

    private Double scores;

    private Long amountOfGames;

    public AggregateScoresDto(String username, Double scores, Long amountOfGames) {
        this.username = username;
        this.scores = scores;
        this.amountOfGames = amountOfGames;
    }
}
