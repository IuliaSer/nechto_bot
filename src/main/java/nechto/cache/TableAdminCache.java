package nechto.cache;

import lombok.Getter;
import nechto.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class TableAdminCache {
    private final Map<Long, Long> map = new ConcurrentHashMap<>();

    public void saveAdminTable(long adminId, long tableId) {
        map.put(adminId, tableId);
    }

    public long get(long adminId) {
        Long tableId = map.get(adminId);
        if (tableId == null) {
            throw new EntityNotFoundException("Table не найден. Пожалуйста сначала создайте стол командой create_table");
        }
        return tableId;
    }
}
