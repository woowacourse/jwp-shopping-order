package cart.exception.badrequest.product;

import cart.exception.badrequest.BadRequestException;

public class ProductPriceException extends BadRequestException {

    public ProductPriceException(String message) {
        super(message);
    }
}
