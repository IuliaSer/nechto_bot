package nechto.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import nechto.enums.Authority;

@Data
@AllArgsConstructor
public class ResponseUserDto {
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    private String username;

    private Authority authority;
}

