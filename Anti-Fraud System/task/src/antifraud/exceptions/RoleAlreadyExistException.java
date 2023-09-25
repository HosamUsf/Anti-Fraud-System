package antifraud.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)

public class RoleAlreadyExistException extends  RuntimeException {
    public RoleAlreadyExistException(String message) {
        super(message + " is already provided");
    }
}
