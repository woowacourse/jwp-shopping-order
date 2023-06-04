package cart.exception;

import cart.domain.member.Member;
import cart.domain.point.Point;
import cart.domain.product.Price;

public class PointException extends RuntimeException {

    public PointException(final String message) {
        super(message);
    }

    public static class NotFound extends PointException {
        public NotFound(final Member member) {
            super(member.getId() + "ID 멤버의 포인트를 찾을 수 없습니다.");
        }
    }

    public static class BadRequest extends PointException {
        public BadRequest(final Point memberPoint) {
            super("멤버가 소유한 포인트 (" + memberPoint.getPoint() + ") 보다 더 많은 포인트를 사용할 수 없습니다.");
        }

        public BadRequest(final Price price) {
            super("총 구매 가격보다 (" + price.price() + ") 더 많은 포인트는 사용할 수 없습니다.");
        }
    }
}
