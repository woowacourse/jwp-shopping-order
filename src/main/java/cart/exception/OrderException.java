package cart.exception;

import cart.domain.Member;
import cart.domain.Order;

public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }

    public static class IllegalMember extends OrderException {

        public IllegalMember(Order order, Member member) {
            super("해당 유저가 접근할 수 없는 Order 입니다.; orderId=" + order.getId() + ", memberId="
                + member.getId());
        }
    }

    public static class NotFound extends OrderException {

        public NotFound(Long orderId) {
            super("해당 아이디의 Order를 찾을 수 없습니다.; orderId=" + orderId);
        }
    }

    public static class EmptyItemInput extends OrderException {

        public EmptyItemInput() {
            super("주문에 필요한 장바구니 상품 id가 입력되지 않았습니다.");
        }
    }
}
