package nechto.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nechto.dto.response.ResponseScoresDto;
import nechto.entity.Scores;
import nechto.enums.Status;
import nechto.exception.EntityNotFoundException;
import nechto.exception.FlamethrowerDisbalanceException;
import nechto.mappers.ScoresMapper;
import nechto.repository.ScoresRepository;
import nechto.status.StatusProcessor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;
import static nechto.enums.Status.ANTI_HUMAN_FLAMETHROWER;
import static nechto.enums.Status.BURNED;
import static nechto.enums.Status.FLAMETHROWER;
import static nechto.enums.Status.LOOSE;
import static nechto.enums.Status.NECHTO;

@Service
@RequiredArgsConstructor
@Transactional
public class ScoresServiceImpl implements ScoresService {
    private final ScoresRepository scoresRepository;
    private final ScoresMapper scoresMapper;
    private final StatusProcessor statusProcessor;

    @Override
    public Scores findByUserIdAndGameId(long userId, long gameId) {
        return scoresRepository.findByUserIdAndGameId(userId, gameId)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Для введенного пользователя отсутствуют очки", userId, gameId)));
    }

    @Override
    public List<Scores> findAllByGameId(long gameId) {
        return scoresRepository.findAllByGameId(gameId);
    }

    @Override
    public void addStatus(Status status, Long userId, Long gameId) {
        Scores scores = this.findByUserIdAndGameId(userId, gameId);
        List<Status> statuses = scores.getStatuses();
        statuses.add(status);
        scoresRepository.save(scores);
    }

    @Override
    public void deleteStatus(Status status, Long userId, Long gameId) {
        Scores scores = this.findByUserIdAndGameId(userId, gameId);
        List<Status> statuses = scores.getStatuses();
        statuses.remove(status);
        scoresRepository.save(scores);
    }

    @Override
    public List<ResponseScoresDto> countAndSaveAll(Long gameId) { //убрать return value
        List<Scores> scoresList = scoresRepository.findAllByGameId(gameId);
        int amountOfFlamethrowersByGame = 0;
        int amountOfBurnedByGame = 0;

        for (Scores scores : scoresList) {
            List<Status> statuses = scores.getStatuses();
            float results = 0;

            for (Status status : statuses) {
                if (FLAMETHROWER.equals(status) || ANTI_HUMAN_FLAMETHROWER.equals(status)) {
                    amountOfFlamethrowersByGame++;
                }
                if (BURNED.equals(status) || (NECHTO.equals(status) && statuses.contains(LOOSE))) {
                    amountOfBurnedByGame++;
                }
                results += statusProcessor.processStatus(statuses, scoresList, status);
            }
            scores.setScores(results);
            scoresRepository.save(scores);
        }
//        if (amountOfFlamethrowersByGame != amountOfBurnedByGame) {
//            throw new FlamethrowerDisbalanceException("Количество сожженых и использованных огнеметов не совпадает! " +
//                    "Пожалуйста измените игру!");
//        }
        return scoresMapper.convertToListResponseScoresDto(scoresList);
    }

    @Override
    public void deleteAllStatuses(Scores scores) {
        scores.getStatuses().clear();
        scoresRepository.save(scores);
    }

    @Override
    public void deleteById(long id) {
        scoresRepository.deleteById(id);
    }

    @Override
    public List<Scores> findAllByGameIds(List<Long> gameIds) {
        return scoresRepository.findAllByGameIds(gameIds);
    }
}
