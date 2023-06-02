package cart.exception;

import cart.domain.Member;
import cart.domain.Order;

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

    public static class IllegalMember extends OrderException {
        public IllegalMember(final Order order, final Member member) {
            super("Illegal member attempts to order; cartItemId=" + order.getId() + ", memberId=" + member.getId());
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
}
