package nechto.service;

import lombok.RequiredArgsConstructor;
import nechto.dto.response.ResponseUserDto;
import org.springframework.stereotype.Component;

import static nechto.enums.Authority.ROLE_ADMIN;
import static nechto.enums.Authority.ROLE_OWNER;

@Component
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final UserService userService;

    @Override
    public boolean checkIsAdmin(long userId) {
        ResponseUserDto responseUserDto = userService.findById(userId);
        String errorMessage = "Access denied. This action is available only to admin";

        if (!ROLE_ADMIN.equals(responseUserDto.getAuthority())) {
            throw new RuntimeException(errorMessage);
        }
        return true;
    }

    @Override
    public boolean checkIsOwner(long userId) {
        ResponseUserDto responseUserDto = userService.findById(userId);
        String errorMessage = "Access denied. This action is available only to owner";

        if (!ROLE_OWNER.equals(responseUserDto.getAuthority())) {
            throw new RuntimeException(errorMessage);
        }
        return true;
    }
}
