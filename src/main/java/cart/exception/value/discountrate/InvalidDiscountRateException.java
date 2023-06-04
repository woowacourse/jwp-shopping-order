package cart.exception.value.discountrate;

import cart.exception.value.ValueException;

public class InvalidDiscountRateException extends ValueException {

    private static final String INVALID_DISCOUNT_RATE_EXCEPTION = "할인율은 0%와 100%사이여야 합니다.";

    public InvalidDiscountRateException() {
        super(INVALID_DISCOUNT_RATE_EXCEPTION);
    }
}
