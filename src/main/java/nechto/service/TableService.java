package nechto.service;

import nechto.entity.Table;

import java.util.List;

public interface TableService {
    void addUser(long tableId, long userId);

    Table save(Table table);

    Table findById(long tableId);

    List<Table> findByToday();
}
