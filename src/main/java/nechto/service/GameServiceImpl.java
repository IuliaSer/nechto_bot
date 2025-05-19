package nechto.service;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestGameDto;
import nechto.dto.response.ResponseGameDto;
import nechto.entity.Game;
import nechto.entity.Scores;
import nechto.entity.User;
import nechto.mappers.GameMapper;
import nechto.repository.GameRepository;
import nechto.repository.ScoresRepository;
import nechto.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    private final UserRepository userRepository;

    private final ScoresRepository scoresRepository;

    private final GameMapper gameMapper;

    @Override
    public ResponseGameDto save(RequestGameDto gameDto) {
        List<User> users = new ArrayList<>();

        for (Long userId: gameDto.getUserIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %s not found", userId)));
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
//        List<Scores> scores = scoresRepository.findAllByGameId(24L);
//        int size = 0;
//        if (!scores.isEmpty()) {
//            size = scores.get(0).getStatuses().size();
//        }
//        System.out.println(size);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %s not found", userId)));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Game with id %s not found", gameId)));

        game.getUsers().add(user);
        for (Scores s : game.getScores()) {
            System.out.println("Statuses before save: " + s.getStatuses());
//            s.getStatuses().size(); // lazy load statuses
        }
        Game game1 = gameRepository.save(game);
        return gameMapper.convertToResponseGameDto(game1);
    }

    @Override
    public void deleteUserFromGame(Long gameId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %s not found", userId)));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Game with id %s not found", gameId)));
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
