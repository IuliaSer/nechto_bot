package nechto.service;

import nechto.dto.request.RequestUserDto;
import nechto.dto.response.ResponseUserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ResponseUserDto save(RequestUserDto userDto);

    ResponseUserDto findByUsernameOrThrow(String login);

    List<ResponseUserDto> findAll();

    List<ResponseUserDto> findAllByGameId(Long gameId);

    void deleteUser(Long userId);

    void makeAdmin(Long userId);

    void makeUser(Long userId);

    Optional<ResponseUserDto> findById(Long id);
}
