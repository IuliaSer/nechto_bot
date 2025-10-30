package nechto.repository;

import nechto.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

//    @EntityGraph(value = "game_entity_graph")
    List<Game> findAll();

//    @EntityGraph(attributePaths = {"users"})

    Optional<Game> findById(@Param("gameId") Long gameId);

    Optional<Game> findTopByScores_User_IdOrderByIdDesc(Long userId);

    @Query("select g from Game g where g.date >= :start and g.date < :end")
    List<Game> findAllByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Game> findAllByDate(LocalDateTime day);
}
