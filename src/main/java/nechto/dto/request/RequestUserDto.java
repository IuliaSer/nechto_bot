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

    private String name;

    @NotNull(message = "Username should not be null")
    @Size(min = 2, max = 20, message = "Username should have expected size")
    private String username;

    private Authority authority;
}

