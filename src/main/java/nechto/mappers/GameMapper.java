package nechto.mappers;

import nechto.dto.GameDto;
import nechto.entity.Game;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapper {
    GameDto convertToResponseGameDto(Game game);

    Game convertToGame(GameDto game);

    List<GameDto> convertToListResponseGameDto(List<Game> games);

}
