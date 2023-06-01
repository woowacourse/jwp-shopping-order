package cart.exception;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
