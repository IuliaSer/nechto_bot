package nechto.service;

import jakarta.validation.Valid;
import nechto.dto.request.RequestUserDto;
import nechto.dto.response.ResponseUserDto;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
public interface UserService {
    ResponseUserDto save(@Valid RequestUserDto userDto);

    ResponseUserDto findByUsernameOrThrow(String login);

    List<ResponseUserDto> findAll();

    List<ResponseUserDto> findAllByGameId(Long gameId);

    void deleteUser(Long userId);

    void makeAdmin(Long userId);

    void makeUser(Long userId);

    Optional<ResponseUserDto> findById(Long id);
}
