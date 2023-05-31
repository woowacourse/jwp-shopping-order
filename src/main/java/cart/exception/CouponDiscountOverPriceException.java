package cart.exception;

public class CouponDiscountOverPriceException extends RuntimeException {

    public CouponDiscountOverPriceException(final String message) {
        super(message);
    }
}
