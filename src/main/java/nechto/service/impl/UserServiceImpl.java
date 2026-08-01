package nechto.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestUserDto;
import nechto.dto.UserDto;
import nechto.entity.User;
import nechto.exception.EntityNotFoundException;
import nechto.exception.EntityAlreadyExistsException;
import nechto.mappers.UserMapper;
import nechto.repository.UserRepository;
import nechto.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static nechto.enums.Authority.ROLE_ADMIN;
import static nechto.enums.Authority.ROLE_USER;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDto save(RequestUserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new EntityAlreadyExistsException(format("Пользователь с таким ником %s уже существует", userDto.getUsername()));
        }
        return userMapper.convertToResponseUserDto(userRepository.save(userMapper.convertToUser(userDto)));
    }

    @Override
    public UserDto findByUsernameOrThrow(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException(format("Пользователь с ником %s не существует", username));
        }
        return userMapper.convertToResponseUserDto(user);
    }

    @Override //mb ubrat
    public Optional<UserDto> findByIdUserDto(Long id) {
        return userRepository.findById(id).map(userMapper::convertToResponseUserDto);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь не существует"));
    }

    @Override
    public List<UserDto> findAllAdmins() {
        return userRepository.findAll().stream()
                .filter(u -> ROLE_ADMIN.equals(u.getAuthority()))
                .map(userMapper::convertToResponseUserDto)
                .toList();
    }

//    @Override
//    public Optional<ResponseUserDto> getByIdOrThrow(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
//        return Optional.ofNullable(userMapper.convertToResponseUserDto(user));
//    }

    @Override
    public List<UserDto> findAll() {
        return userMapper.convertToListOfResponseUserDto(userRepository.findAll());
    }

    @Override
    public List<UserDto> findAllByGameId(Long gameId) {
        return userMapper.convertToListOfResponseUserDto(userRepository.findAllByGameId(gameId));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void makeAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %s not found", userId)));
        user.setAuthority(ROLE_ADMIN);
        userRepository.save(user);
    }

    @Override
    public void makeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %s not found", userId)));
        user.setAuthority(ROLE_USER);
        userRepository.save(user);
    }

}
