package cart.exception.badrequest.order;

import cart.exception.badrequest.BadRequestException;

public class OrderDeliveryFeeException extends BadRequestException {

    public OrderDeliveryFeeException(String message) {
        super(message);
    }
}
