package cart.exception;

import org.springframework.http.HttpStatus;

public class ShoppingOrderException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;

    public ShoppingOrderException(ErrorStatus errorStatus) {
        this.httpStatus = errorStatus.getHttpStatus();
        this.message = errorStatus.getMessage();
        this.code = errorStatus.getCode();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
