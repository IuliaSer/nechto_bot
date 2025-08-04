package nechto.service;

import lombok.RequiredArgsConstructor;
import nechto.dto.response.ResponseUserDto;
import org.springframework.stereotype.Component;

import static nechto.enums.Authority.*;

@Component
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final UserService userService;

    @Override
    public boolean isAdmin(long userId) {
        ResponseUserDto responseUserDto = userService.findById(userId);
//        String errorMessage = "Access denied. This action is available only to admin";
//
        return ROLE_ADMIN.equals(responseUserDto.getAuthority());
    }

    @Override
    public boolean isOwner(long userId) {
        ResponseUserDto responseUserDto = userService.findById(userId);
//        String errorMessage = "Access denied. This action is available only to owner";

        return ROLE_OWNER.equals(responseUserDto.getAuthority());
    }

    @Override
    public boolean isUser(long userId) {
        ResponseUserDto responseUserDto = userService.findById(userId);

        return ROLE_USER.equals(responseUserDto.getAuthority());
    }
}
