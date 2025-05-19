package nechto.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import nechto.enums.Authority;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RequestUserDto {
    private Long id;
//    @NotNull(message = "Name should not be null")
//    @Size(min = 2, max = 20, message = "Name should have expected size")
    private String name;

    @NotNull(message = "Username should not be null")
    @Size(min = 2, max = 20, message = "Username should have expected size")
    private String username;

//    @NotNull(message = "Password should not be null")
//    @Size(min = 2, max = 20, message = "Password should have expected size")
    private String password;

    private Authority authority;
}

