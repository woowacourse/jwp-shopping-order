package cart.exception;

public class OrderException extends CustomException {
    public OrderException(final ErrorMessage message) {
        super(message);
    }
}
