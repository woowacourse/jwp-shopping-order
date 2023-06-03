package cart.exception;

public class ProductException extends RuntimeException {

    public ProductException(final String message) {
        super(message);
    }

    public static class NotFoundProduct extends ProductException {
        public NotFoundProduct(final Long productId) {
            super(productId + "ID 를 가진 상품을 찾을 수 없습니다");
        }
    }
}
