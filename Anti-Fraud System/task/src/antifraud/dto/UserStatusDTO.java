package antifraud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatusDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;

    @NotEmpty(message = "User status mustn't be empty")
    @NotNull(message = "User status mustn't be null")
    private String status;

    private UserStatusDTO(String status) {
        this.status = status;
    }
}
