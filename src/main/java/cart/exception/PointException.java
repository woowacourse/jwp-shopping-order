package cart.exception;

public class PointException extends RuntimeException {

    public PointException(String message) {
        super(message);
    }

    public static class InvalidUsedPoint extends PointException {

        public InvalidUsedPoint() {
            super("사용하려는 포인트가 소유 포인트보다 많습니다.");
        }
    }
}
