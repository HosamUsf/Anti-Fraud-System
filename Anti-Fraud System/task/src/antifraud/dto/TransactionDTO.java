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
public class TransactionDTO {

    @NotEmpty(message = "result mustn't be empty")
    @NotNull(message = "result mustn't be null")
    private String result;

    @NotBlank
    private String info ;

}
