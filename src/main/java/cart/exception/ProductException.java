package cart.exception;

public class ProductException extends RuntimeException {
    public ProductException(String message) {
        super(message);
    }

    public static class NotFound extends ProductException {
        public NotFound() {
            super("상품을 찾을 수 없습니다.");
        }
    }
}
