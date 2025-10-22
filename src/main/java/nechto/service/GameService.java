package nechto.service;

import nechto.dto.response.ResponseGameDto;
import nechto.dto.request.RequestGameDto;
import nechto.entity.Game;

import java.util.List;
import java.util.Optional;

public interface GameService {
    ResponseGameDto save(RequestGameDto game);

    ResponseGameDto addUser(Long gameId, Long userId);

    void deleteUserFromGame(Long gameId, Long userId);

    List<ResponseGameDto> findAll();

    ResponseGameDto findById(Long id);

    void delete(Long gameId);

    Optional<Game> findLastGameByUserId(Long userId);
}
