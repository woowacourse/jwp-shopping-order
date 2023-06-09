package cart.product.exception;

public class NotFoundProductException extends RuntimeException {

    private static final String message = "존재하지 않는 상품입니다";

    public NotFoundProductException() {
        super(message);
    }
}
