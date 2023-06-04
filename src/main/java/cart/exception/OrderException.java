package cart.exception;

import cart.domain.Member;
import cart.domain.Order;

public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(Order order, Member member) {
            super("Illegal member attempts to order; orderId=" + order.getId() + ", memberId=" + member.getId());
        }
    }
}
