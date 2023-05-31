package cart.exception;

public class OrderException extends RuntimeException {

    public OrderException(final String message) {
        super(message);
    }

    public static class OutOfDatedProductPrice extends OrderException {

        public OutOfDatedProductPrice() {
            super("Out of dated product price; please retry order request");
        }
    }
}
