package cart.exception;

import cart.domain.Member;
import cart.domain.Order;
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
}
