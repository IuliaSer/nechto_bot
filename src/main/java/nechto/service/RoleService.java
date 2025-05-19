package nechto.service;

public interface RoleService {
    boolean checkIsAdmin(long userId);

    boolean checkIsOwner(long userId);
}
