package cart.exception;

import cart.domain.Member;
import cart.domain.order.Order;

public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }

    public static class IllegalMember extends OrderException {
        public IllegalMember(Order order, Member member) {
            super("Illegal member attempts to order; orderId=" + order.getId() + ", memberId=" + member.getId());
        }
    }

    public static class NoCartItem extends OrderException {
        public NoCartItem(){
            super("주문하려는 상품이 없습니다.");
        }
    }
}
