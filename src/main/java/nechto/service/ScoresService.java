package nechto.service;

import nechto.dto.response.ResponseScoresDto;
import nechto.dto.request.RequestScoresDto;
import nechto.entity.Scores;
import nechto.enums.Status;

import java.util.Collection;
import java.util.List;

public interface ScoresService {
    Scores findByUserIdAndGameId(long userId, long gameId);

    List<Scores> findAllByGameId(long gameId);

    void addStatus(Status status, Long userId, Long gameId);

    void deleteStatus(Status status, Long userId, Long gameId);

    List<ResponseScoresDto> countAndSaveAll(Long gameId);

    ResponseScoresDto update(RequestScoresDto scores);

    void deleteAllStatuses(Scores scores);

    void deleteById(long id);

    List<Scores> findAllByGameIds(List<Long> gameIds);
}
