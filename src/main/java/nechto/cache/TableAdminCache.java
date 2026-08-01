package nechto.cache;

import nechto.entity.Table;
import nechto.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TableAdminCache {
    private final Map<Long, Table> map = new ConcurrentHashMap<>();

    public void saveAdminTable(long adminId, Table table) {
        map.put(adminId, table);
    }

    public Table get(long adminId) {
        Table table = map.get(adminId);
        if (table == null) {
            throw new EntityNotFoundException("Table не найден. Пожалуйста сначала создайте стол командой create_table");
        }
        return table;
    }
}
