package nechto.repository;

import nechto.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<Table, Long> {

    @Query("""
           select distinct t
           from Table t
           left join fetch t.currentUsers
           where t.id = :tableId
           """)
    Optional<Table> findByIdWithCurrentUsers(Long tableId);

    List<Table> findAllByDateGreaterThanEqualAndDateLessThan(LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
