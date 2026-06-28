package nechto.cache;

import nechto.entity.Table;
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
        return map.get(adminId);
    }
}
