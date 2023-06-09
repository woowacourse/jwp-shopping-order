package cart.exception.forbidden;

import cart.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApplicationException {

    private static final String ERROR_TYPE = "FORBIDDEN";

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String getMessage() {
        return ERROR_TYPE;
    }
}
