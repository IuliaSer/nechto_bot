package nechto.service;

import nechto.dto.request.RequestGameDto;
import nechto.dto.GameDto;
import nechto.entity.Game;
import nechto.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GameService {
    GameDto save(RequestGameDto game);

    GameDto addUser(Long gameId, Long userId);

    GameDto addUsers(Long gameId, List<User> users);

    void deleteUserFromGame(Long gameId, Long userId);

    List<GameDto> findAll();

    Game findById(Long id);

    void delete(Long gameId);

    Optional<Game> findLastGameByUserId(Long userId);

    List<Long> findAllByDate(LocalDateTime startPeriod, LocalDateTime endPeriod);

}
