package nechto.service;

import jakarta.validation.Valid;
import nechto.dto.request.RequestUserDto;
import nechto.dto.UserDto;
import nechto.entity.User;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
public interface UserService {
    UserDto save(@Valid RequestUserDto userDto);

    UserDto findByUsernameOrThrow(String login);

    List<UserDto> findAll();

    List<UserDto> findAllByGameId(Long gameId);

    void deleteUser(Long userId);

    void makeAdmin(Long userId);

    void makeUser(Long userId);

    Optional<UserDto> findByIdUserDto(Long id);


    User findById(Long id);

    List<UserDto>  findAllAdmins();
}
