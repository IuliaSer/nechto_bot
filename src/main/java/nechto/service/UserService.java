package nechto.service;

import nechto.dto.request.FullUserDto;
import nechto.dto.request.RequestUserDto;
import nechto.dto.response.ResponseUserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ResponseUserDto save(RequestUserDto userDto);

    ResponseUserDto findByUsername(String login);

    List<ResponseUserDto> findAll();

    List<ResponseUserDto> findUsersOrderedByGamesAmount();

    void deleteUser(Long userId);

    void makeAdmin(Long userId);

    void makeUser(Long userId);

    ResponseUserDto updateUser(FullUserDto user);

    Optional<ResponseUserDto> findById(Long id);
}
