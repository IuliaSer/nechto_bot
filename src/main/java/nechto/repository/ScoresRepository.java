package nechto.repository;

import nechto.entity.Scores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScoresRepository extends JpaRepository<Scores, Long> {

    List<Scores> findAllByGameId(Long gameId);

    Optional<Scores> findByUserIdAndGameId(Long userId, Long gameId);

    @Query("select s from Scores s where s.game.id in :gameIds")
    List<Scores> findAllByGameIds(List<Long> gameIds);
}
