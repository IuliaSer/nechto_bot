package nechto.mappers;

import nechto.dto.request.RequestUserDto;
import nechto.dto.UserDto;
import nechto.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto convertToResponseUserDto(User user);

    @Mapping(target = "authority", constant = "ROLE_USER")
    User convertToUser(RequestUserDto userDto);

    List<UserDto> convertToListOfResponseUserDto(List<User> users);

}
