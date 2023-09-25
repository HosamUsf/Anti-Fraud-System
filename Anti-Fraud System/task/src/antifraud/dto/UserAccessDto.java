package antifraud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserAccessDto {



    @NotEmpty(message = "Username mustn't be empty")
    @NotNull(message = "Username mustn't be null")
    private String username;

    @NotBlank
    private String operation;
}
