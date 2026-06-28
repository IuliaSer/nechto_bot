package nechto.mappers;

import nechto.dto.ScoresDto;
import nechto.entity.Scores;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScoresMapper {

    ScoresDto convertToResponseScoresDto(Scores scores);

    List<ScoresDto> convertToListResponseScoresDto(List<Scores> scores);

}
