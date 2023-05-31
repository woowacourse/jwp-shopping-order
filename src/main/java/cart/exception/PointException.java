package cart.exception;

public class PointException extends RuntimeException {

    public PointException(String message) {
        super(message);
    }

    public static class PointInconsistencyException extends PointException {

        public PointInconsistencyException(String message) {
            super(message);
        }
    }

    public static class InvalidPointException extends PointException {

        public InvalidPointException(String message) {
            super(message);
        }
    }
}
