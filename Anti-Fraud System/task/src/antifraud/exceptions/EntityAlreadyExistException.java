package antifraud.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityAlreadyExistException extends  RuntimeException{
    public EntityAlreadyExistException(String message) {
        super("Exception: Entity  with username " + message + " is already exist");
    }

}
