package cart.exception;

import org.springframework.http.HttpStatus;

public abstract class ApplicationException extends RuntimeException {

    public abstract HttpStatus getHttpStatus();
    
    public abstract String getMessage();
}
