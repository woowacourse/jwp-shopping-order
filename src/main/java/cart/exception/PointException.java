package cart.exception;

public abstract class PointException extends RuntimeException {

    private PointException(String message) {
        super(message);
    }

    public static class InvalidPolicy extends PointException {

        public InvalidPolicy() {
            super("사용할 수 있는 포인트 이상을 지정했습니다.");
        }
    }
}
