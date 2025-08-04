package nechto.service;

import nechto.enums.Authority;

public interface MenuService {
    void refreshCommands(long userId, Authority authority);
}
