package cart.exception.badrequest.product;

import cart.exception.badrequest.BadRequestException;

public class ProductNameException extends BadRequestException {

    public ProductNameException(String message) {
        super(message);
    }
}
