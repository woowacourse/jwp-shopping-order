package cart.exception;

public abstract class ProductException extends RuntimeException {

    private ProductException(String message) {
        super(message);
    }

    public static class NotFound extends ProductException {

        public NotFound(Long id) {
            super("해당 상품을 찾을 수 없습니다 : " + id);
        }
    }
}
