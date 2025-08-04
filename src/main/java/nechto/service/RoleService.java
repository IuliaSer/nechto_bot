package nechto.service;

public interface RoleService {
    boolean isAdmin(long userId);

    boolean isOwner(long userId);

    boolean isUser(long userId);
}
