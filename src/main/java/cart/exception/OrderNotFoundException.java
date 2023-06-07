package cart.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends ApplicationException{

    public OrderNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
