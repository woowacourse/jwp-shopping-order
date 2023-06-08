package cart.exception;

public class PointException extends ApiException {

    public PointException(String message) {
        super(message);
    }

    public static class InvalidRange extends PointException {

        public InvalidRange(long point) {
            super("잘못된 포인트입니다. " +
                    "point : " + point);
        }
    }
}
