package cart.domain.cartitem;

import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;

public class Quantity {

    private Long quantity;

    public Quantity(Long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(Long quantity) {
        if (quantity <= 0) {
            throw new CartException(ErrorCode.QUANTITY_INVALID);
        }
    }

    public Long getQuantity() {
        return quantity;
    }
}
