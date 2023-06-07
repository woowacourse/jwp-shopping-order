package cart.exception;

import org.springframework.http.HttpStatus;

public class InvalidURLException extends ApplicationException {

    public InvalidURLException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
