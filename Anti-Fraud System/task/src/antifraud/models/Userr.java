package antifraud.models;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Userr {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonView(Views.Public.class)
    private long id ;

    @JsonView(Views.Public.class)
    @NotBlank
    private String name;

    @JsonView(Views.Public.class)
    @NotBlank
    private String username;

    @JsonView(Views.Private.class)
    @NotBlank
    private String password;

    @JsonView(Views.Public.class)
    private String role ;

    @JsonView(Views.Private.class)
    private Boolean locked ;
}
