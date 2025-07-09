package nechto.dto;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class ScoresDto {

    private String username;

    private float scores;

    private String status;

    private float flamethrowerScores;

    private List<String> opjStatusScores;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScoresDto that = (ScoresDto) o;
        return username.equals(that.username); // и другие поля при необходимости
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public String getFlamethrowerScoresWithPrecision() {
        return String.format("%.2f", flamethrowerScores);
    }

}
