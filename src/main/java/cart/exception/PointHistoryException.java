package cart.exception;

public class PointHistoryException extends RuntimeException {
    public PointHistoryException(final String message) {
        super(message);
    }

    public static class NotFound extends PointHistoryException {
        public NotFound(final Long orderId) {
            super(orderId + "ID 주문를 통해 누적된 포인트 이력을 찾을 수 없습니다.");
        }
    }
}
