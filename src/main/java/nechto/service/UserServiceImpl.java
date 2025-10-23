package nechto.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nechto.dto.request.FullUserDto;
import nechto.dto.request.RequestUserDto;
import nechto.dto.response.ResponseUserDto;
import nechto.entity.User;
import nechto.exception.EntityNotFoundException;
import nechto.exception.EntityAlreadyExistsException;
import nechto.mappers.UserMapper;
import nechto.repository.UserRepository;
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
    public ResponseUserDto save(RequestUserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new EntityAlreadyExistsException("A user with this login already exists");
        }
//        userDto.setAuthority(ROLE_ADMIN);
        return userMapper.convertToResponseUserDto(userRepository.save(userMapper.convertToUser(userDto)));
    }

    @Override
    public ResponseUserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException(String.format("Пользователь с ником %s не существует", username));
        }
        return userMapper.convertToResponseUserDto(user);
    }

    @Override
    public Optional<ResponseUserDto> findById(Long id) {
        return userRepository.findById(id).map(userMapper::convertToResponseUserDto);
    }

//    @Override
//    public Optional<ResponseUserDto> getByIdOrThrow(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
//        return Optional.ofNullable(userMapper.convertToResponseUserDto(user));
//    }

    @Override
    public List<ResponseUserDto> findAll() {
        return userMapper.convertToListOfResponseUserDto(userRepository.findAll());
    }

    @Override
    public List<ResponseUserDto> findUsersOrderedByGamesAmount() {
        return userMapper.convertToListOfResponseUserDto(userRepository.findAllOrderedByGameAmount());
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

    @Override
    public ResponseUserDto updateUser(FullUserDto userDto) {
        User user = User.builder()
                        .id(userDto.getId())
                        .name(userDto.getName())
                        .username(userDto.getUsername())
                        .authority(userDto.getAuthority())
                        .build();
        return userMapper.convertToResponseUserDto(userRepository.save(user));
    }

}
