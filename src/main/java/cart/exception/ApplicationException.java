package cart.exception;

import org.springframework.http.HttpStatus;

public abstract class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public abstract HttpStatus status();

    @Override
    public String toString() {
        return "[ERROR] status : "+status() +"\nmessage : "+getMessage();
    }
}
