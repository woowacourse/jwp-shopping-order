package cart.exception;

public class OrderException extends RuntimeException {

    public OrderException(final String message) {
        super(message);
    }

    public static class IllegalUsePoint extends OrderException {
        public IllegalUsePoint(final int remainPoint, final int usePoint) {
            super("사용 포인트가 잔여 포인트보다 많습니다. 잔여 포인트: " + remainPoint + " 사용 포인트: " + usePoint);
        }

        public IllegalUsePoint(final String message) {
            super(message);
        }
    }

    public static class MismatchedTotalPrice extends OrderException {
        public MismatchedTotalPrice(final String message) {
            super(message);
        }
    }

    public static class MismatchedTotalProductPrice extends OrderException {
        public MismatchedTotalProductPrice(final String message) {
            super(message);
        }
    }

    public static class InsufficientStock extends OrderException {
        public InsufficientStock(final String message) {
            super(message);
        }
    }
}
