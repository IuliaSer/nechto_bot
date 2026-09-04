package nechto.service.impl;

import lombok.AllArgsConstructor;
import nechto.entity.Table;
import nechto.exception.EntityNotFoundException;
import nechto.repository.TableRepository;
import nechto.service.TableService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;

    @Override
    public void addUser(long tableId, long userId) {
        tableRepository.findById(tableId);
    }

    @Override
    public Table save(Table table) {
        return tableRepository.save(table);
    }

    @Override
    public Table findById(long tableId) {
        return tableRepository.findById(tableId).orElseThrow(() -> new EntityNotFoundException("Table not found"));
    }

    @Override
    public List<Table> findByToday() {

        LocalDate day = LocalDate.now();

        return tableRepository.findAllByDateGreaterThanEqualAndDateLessThan(day.atStartOfDay(),
                        day.plusDays(1).atStartOfDay());
    }
}
