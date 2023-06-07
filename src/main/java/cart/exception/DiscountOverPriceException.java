package cart.exception;

public class DiscountOverPriceException extends RuntimeException {

    public DiscountOverPriceException(final String message) {
        super(message);
    }
}
