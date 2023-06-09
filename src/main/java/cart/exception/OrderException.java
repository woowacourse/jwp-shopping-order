package cart.exception;

import cart.domain.member.Member;

public class OrderException extends RuntimeException {

    public OrderException(final String message) {
        super(message);
    }

    public static class NotFound extends OrderException {
        public NotFound(final Long id) {
            super(id + " ID를 가진 주문을 찾을 수 없습니다.");
        }
    }

    public static class IllegalMember extends OrderException {
        public IllegalMember(final Member member) {
            super(member.getId() + " ID의 멤버의 주문이 아닙니다.");
        }
    }
}
