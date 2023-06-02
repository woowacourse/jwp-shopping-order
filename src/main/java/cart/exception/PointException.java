package cart.exception;

public class PointException extends RuntimeException {

    public PointException(final String message) {
        super(message);
    }

    public static class NotEnough extends PointException {
        public NotEnough(final int memberPoint, final int usedPoint) {
            super("보유한 포인트가 부족합니다. 보유 포인트: " + memberPoint + ", 사용 포인트: " + usedPoint);
        }
    }

    public static class NegativePoint extends PointException {
        public NegativePoint(final int point) {
            super("포인트는 음수일 수 없습니다. point: " + point);
        }
    }
}
