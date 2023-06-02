package cart.exception;

import cart.domain.Order;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }

    public static class NotFound extends PaymentException {
        public NotFound(Order order) {
            super("해당 주문에 대한 결제기록이 존재하지 않습니다. orderId=" + order.getId());
        }

    }
}
