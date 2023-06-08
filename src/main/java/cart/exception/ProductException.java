package cart.exception;

public class ProductException extends RuntimeException {
    public ProductException(final String message) {
        super(message);
    }

    public static class NotFoundException extends ProductException {
        public NotFoundException(final Long productId) {
            super("해당 아이디의 Product를 찾을 수 없습니다; productId=" + productId);
        }
    }
}
