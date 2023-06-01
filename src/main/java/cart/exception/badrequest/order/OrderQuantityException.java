package cart.exception.badrequest.order;

import cart.exception.badrequest.BadRequestException;

public class OrderQuantityException extends BadRequestException {

    public OrderQuantityException(String message) {
        super(message);
    }
}
