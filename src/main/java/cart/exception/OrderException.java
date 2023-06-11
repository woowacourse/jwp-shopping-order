package cart.exception;

import cart.domain.Member;
import cart.domain.Order;

public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }

    public static class IllegalMember extends OrderException {
        public IllegalMember(Order order, Member member) {
            super("Illegal member attempts to order; cartItemId=" + order.getId() + ", memberId=" + member.getId());
        }
    }
}
