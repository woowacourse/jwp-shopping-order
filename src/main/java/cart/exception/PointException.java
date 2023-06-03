package cart.exception;

import cart.domain.member.Member;

public class PointException extends RuntimeException {

    public PointException(final String message) {
        super(message);
    }

    public static class NotFound extends PointException {
        public NotFound(final Member member) {
            super(member.getId() + "ID 멤버의 포인트를 찾을 수 없습니다.");
        }
    }
}
