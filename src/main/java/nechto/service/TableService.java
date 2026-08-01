package nechto.service;

import nechto.entity.Table;

public interface TableService {
    void addUser(long tableId, long userId);

    Table save(Table requestTableDto);

    Table findById(long tableId);
}
