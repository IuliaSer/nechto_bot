package nechto.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestGameDto;
import nechto.dto.response.ResponseGameDto;
import nechto.entity.Game;
import nechto.entity.Scores;
import nechto.entity.User;
import nechto.exception.EntityAlreadyExistsException;
import nechto.exception.EntityNotFoundException;
import nechto.mappers.GameMapper;
import nechto.repository.GameRepository;
import nechto.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Transactional
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    private final UserRepository userRepository;

    private final GameMapper gameMapper;

    @Override
    public ResponseGameDto save(RequestGameDto gameDto) {
        Game game = Game.builder()
                .date(gameDto.getDate())
                .build();
        List<Scores> scores = new ArrayList<>();
        for (Long userId: gameDto.getUserIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException(format("User with id %s not found", userId)));
            Scores score = new Scores().setGame(game).setUser(user);
            scores.add(score);
            user.getScores().add(score);
        }
        game.setScores(scores);
        Game gameSaved = gameRepository.save(game);

        return gameMapper.convertToResponseGameDto(gameSaved);
    }

    @Override
    public ResponseGameDto addUser(Long gameId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(format("User with id %s not found", userId)));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException(format("Game with id %s not found", gameId)));

        game.getScores().add(new Scores()
                .setGame(game)
                .setUser(user));
        Game gameSaved;
        try {
            gameSaved = gameRepository.save(game);
        } catch (Exception e) {
            throw new EntityAlreadyExistsException("Пользователь уже добавлен в игру. Введите другого пользователя");
        }
        return gameMapper.convertToResponseGameDto(gameSaved);
    }

    @Override
    public void deleteUserFromGame(Long gameId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(format("User with id %s not found", userId)));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException(format("Game with id %s not found", gameId)));
        Scores scores = new Scores().setUser(user).setGame(game);
        game.getScores().remove(scores);
        gameRepository.save(game);
    }

    @Override
    public List<ResponseGameDto> findAll() {
        return gameMapper.convertToListResponseGameDto(gameRepository.findAll());
    }

    @Override
    public ResponseGameDto findById(Long id) {
        return gameMapper.convertToResponseGameDto(gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Game with id %s not found", id))));
    }

    @Override
    public void delete(Long gameId) {
        gameRepository.deleteById(gameId);
    }

    @Override
    public Optional<Game> findLastGameByUserId(Long userId) {
        return gameRepository.findTopByScores_User_IdOrderByIdDesc(userId);
    }

    @Override
    public List<Long> findAllByDate(LocalDateTime start, LocalDateTime end) {
        return gameRepository.findAllByDateBetween(start, end)
                .stream()
                .map(Game::getId)
                .toList();
    }
}
