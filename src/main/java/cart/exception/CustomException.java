package cart.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final ErrorMessage message;

    public CustomException(final ErrorMessage message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return message.getHttpStatus();
    }

    @Override
    public String getMessage() {
        return message.getMessage();
    }
}
