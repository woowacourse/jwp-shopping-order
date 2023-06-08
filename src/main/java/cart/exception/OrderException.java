package cart.exception;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Point;
import org.springframework.http.HttpStatus;

public class OrderException extends ShoppingCartException {
    public OrderException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static class IllegalMember extends OrderException {
        public IllegalMember(ErrorCode errorCode, Order order, Member member) {
            super(errorCode.getErrorMessage()
                    + " : orderId = "
                    + order.getId()
                    + ", memberId = "
                    + member.getId(), HttpStatus.FORBIDDEN);
        }
    }

    public static class InvalidPoint extends OrderException {
        public InvalidPoint(ErrorCode errorCode, Point savedPoint, Point usedPoint) {
            super(errorCode.getErrorMessage()
                    + " 저장된 포인트 = "
                    + savedPoint.getPoint()
                    + ", 사용한 포인트 = "
                    + usedPoint.getPoint(), HttpStatus.BAD_REQUEST);
        }
    }
}
