package cart.exception.auth;

import cart.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApplicationException {

    private static final String ERROR_TYPE = "UNAUTHORIZED";

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getMessage() {
        return ERROR_TYPE;
    }
}
