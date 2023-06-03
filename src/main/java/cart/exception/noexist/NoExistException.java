package cart.exception.noexist;

import cart.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NoExistException extends ApplicationException {

    private final NoExistErrorType errorType;

    public NoExistException(final NoExistErrorType errorType) {
        this.errorType = errorType;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }

    @Override
    public String getMessage() {
        return errorType.name();
    }
}
