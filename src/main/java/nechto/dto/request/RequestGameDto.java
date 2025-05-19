package nechto.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class RequestGameDto {
    @NotNull(message = "Date should not be null")
    private LocalDateTime date;

    @NotNull(message = "User ids should not be null")
    private List<Long> userIds;
}
