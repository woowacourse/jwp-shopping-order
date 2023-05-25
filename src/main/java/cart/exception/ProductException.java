package cart.exception;

public class ProductException extends RuntimeException {

    public ProductException(String message) {
        super(message);
    }

    public static class InvalidImageUrl extends ProductException {

        public InvalidImageUrl(String message) {
            super(message);
        }
    }

    public static class IllegalId extends ProductException {

        public IllegalId(String message) {
            super(message);
        }
    }

    public static class NotFound extends ProductException {

        public NotFound(String message) {
            super(message);
        }
    }

    public static class InvalidPrice extends ProductException {

        public InvalidPrice(String message) {
            super(message);
        }
    }

    public static class InvalidName extends ProductException {

        public InvalidName(String message) {
            super(message);
        }
    }
}
