package nechto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class GameDto {
    private Long id;

    private Date date;

    private List<UserDto> users;
}
