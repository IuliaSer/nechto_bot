package nechto.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import nechto.enums.Authority;

@Data
@AllArgsConstructor
public class RequestUserDto {
    private Long id;

    @NotNull(message = "Name should not be null")
    @Size(min = 3, max = 15, message = "Имя пользователя должно быть не меньше 3 символов и не больше 15")
    private String name;

    @NotNull(message = "Username should not be null")
    @Size(min = 3, max = 8, message = "Ник пользователя должно быть не меньше 3 символов и не больше 8")
    private String username;

    private Authority authority;
}

