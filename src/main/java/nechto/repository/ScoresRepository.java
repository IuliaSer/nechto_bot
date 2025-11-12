package nechto.repository;

import nechto.dto.AggregateScoresDto;
import nechto.entity.Scores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScoresRepository extends JpaRepository<Scores, Long> {

    List<Scores> findAllByGameId(Long gameId);

    Optional<Scores> findByUserIdAndGameId(Long userId, Long gameId);

    @Query("select s from Scores s where s.game.id in :gameIds")
    List<Scores> findAllByGameIds(List<Long> gameIds);

    @Query("""
    select new nechto.dto.AggregateScoresDto(s.user.username, sum(s.scores), count(s))
    from Scores s
    join s.game g
    where g.date >= :start and g.date < :end
      and s.user.username is not null
    group by s.user.username
    """)
    List<AggregateScoresDto> aggregateByUserBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
