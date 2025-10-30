package nechto.service;

import nechto.dto.request.RequestGameDto;
import nechto.dto.response.ResponseGameDto;
import nechto.entity.Game;

import java.time.LocalDateTime;
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

    List<Long> findAllByDate(LocalDateTime startPeriod, LocalDateTime endPeriod);

}
