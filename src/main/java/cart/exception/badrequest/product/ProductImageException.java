package cart.exception.badrequest.product;

import cart.exception.badrequest.BadRequestException;

public class ProductImageException extends BadRequestException {

    public ProductImageException(String message) {
        super(message);
    }
}
