package nechto.mappers;

import nechto.dto.request.RequestUserDto;
import nechto.dto.response.ResponseUserDto;
import nechto.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ResponseUserDto convertToResponseUserDto(User user);

    @Mapping(target = "authority", constant = "ROLE_USER")
    User convertToUser(RequestUserDto userDto);

    List<ResponseUserDto> convertToListOfResponseUserDto(List<User> users);

}
