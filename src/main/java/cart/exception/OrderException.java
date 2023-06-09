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

    public static class IllegalId extends OrderException {
        public IllegalId(final Long id) {
            super("Illegal order id; id=" + id);
        }
    }

    public static class IllegalOrderStatus extends OrderException {
        public IllegalOrderStatus(final String name) {
            super("illegal Order status name; name =" + name);
        }
    }

    public static class AlreadyCanceledOrder extends OrderException {
        public AlreadyCanceledOrder(final Long id) {
            super("cannot cancel already canceled order; id =" + id);
        }
    }

    public static class AlreadyCompletedOrder extends OrderException {
        public AlreadyCompletedOrder(final long id) {
            super("cannot complete already completed order; id =" + id);
        }
    }
}
