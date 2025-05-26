package nechto.service;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestGameDto;
import nechto.dto.response.ResponseGameDto;
import nechto.entity.Game;
import nechto.entity.User;
import nechto.mappers.GameMapper;
import nechto.repository.GameRepository;
import nechto.repository.ScoresRepository;
import nechto.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Transactional
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    private final UserRepository userRepository;

    private final ScoresRepository scoresRepository;

    private final GameMapper gameMapper;
    private final EntityManager entityManager;

    @Override
    public ResponseGameDto save(RequestGameDto gameDto) {
        List<User> users = new ArrayList<>();

        for (Long userId: gameDto.getUserIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException(format("User with id %s not found", userId)));
            users.add(user);
        }
        Game game = Game.builder()
                .date(gameDto.getDate())
                .users(users)
                .build();
        Game gameSaved = gameRepository.save(game);

        return gameMapper.convertToResponseGameDto(gameSaved);
    }

    @Override
    public ResponseGameDto addUser(Long gameId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(format("User with id %s not found", userId)));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException(format("Game with id %s not found", gameId)));

        game.getUsers().add(user);
        
        Game gameSaved = gameRepository.save(game);
        return gameMapper.convertToResponseGameDto(gameSaved);
    }

    @Override
    public void deleteUserFromGame(Long gameId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(format("User with id %s not found", userId)));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException(format("Game with id %s not found", gameId)));
        game.getUsers().remove(user);
        gameRepository.save(game);
    }

    @Override
    public List<ResponseGameDto> findAll() {
        return gameMapper.convertToListResponseGameDto(gameRepository.findAll());
    }

    @Override
    public ResponseGameDto findById(Long id) {
        return gameMapper.convertToResponseGameDto(gameRepository.findById(id).get());
    }

    @Override
    public void delete(Long gameId) {
        gameRepository.deleteById(gameId);
    }
}
