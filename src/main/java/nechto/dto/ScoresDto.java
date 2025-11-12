package nechto.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Builder
public class ScoresDto {

    private String username;

    private Float scores;

    private String status;

    private Float flamethrowerScores;

    private List<String> opjStatusScores;

    private Float koef;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScoresDto that = (ScoresDto) o;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}
