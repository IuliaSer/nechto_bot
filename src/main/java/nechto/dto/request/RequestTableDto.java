package nechto.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class RequestTableDto {
    private String nameOrNumber;

    private Long adminId;

    @NotNull(message = "Date should not be null")
    private LocalDateTime date;

    @NotNull(message = "User ids should not be null")
    private List<Long> userIds;

    private List<Long> gameIds;

}
