package cart.order.exception.order;

public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
