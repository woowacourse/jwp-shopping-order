package cart.exception.badrequest.order;

import cart.exception.badrequest.BadRequestException;

public class OrderPointException extends BadRequestException {

    public OrderPointException(String message) {
        super(message);
    }
}
