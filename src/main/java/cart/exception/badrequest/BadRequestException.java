package cart.exception.badrequest;

import cart.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ApplicationException {

    private final BadRequestErrorType errorType;

    public BadRequestException(final BadRequestErrorType errorType) {
        this.errorType = errorType;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return errorType.name();
    }
}
