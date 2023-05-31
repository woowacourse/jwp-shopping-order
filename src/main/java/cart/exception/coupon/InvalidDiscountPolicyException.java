package cart.exception.coupon;

import cart.exception.common.CartException;

public class InvalidDiscountPolicyException extends CartException {

    public InvalidDiscountPolicyException(final String message) {
        super(message);
    }
}
