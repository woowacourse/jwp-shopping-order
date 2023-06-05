package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member.Member;
import cart.domain.Order.Order;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }

    public static class IllegalMemberCart extends ForbiddenException {
        public IllegalMemberCart(CartItem cartItem, Member member) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }

    public static class IllegalMemberOrder extends ForbiddenException {
        public IllegalMemberOrder(Order order, Member member) {
            super("Illegal member attempts to order; orderId=" + order.getId() + ", memberId=" + member.getId());
        }
    }
}
