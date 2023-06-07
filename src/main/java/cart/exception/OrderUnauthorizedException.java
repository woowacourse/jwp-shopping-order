package cart.exception;

import org.springframework.http.HttpStatus;

public class OrderUnauthorizedException extends ApplicationException {

    public OrderUnauthorizedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
