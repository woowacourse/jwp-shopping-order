package cart.exception;

public class PaymentAmountNotEqualException extends IllegalArgumentException {

    public PaymentAmountNotEqualException(final String message) {
        super(message);
    }
}
