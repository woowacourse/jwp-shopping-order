package cart.exception;

import org.springframework.http.HttpStatus;

public class OrderCartMismatchException extends ApplicationException{

    public OrderCartMismatchException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
