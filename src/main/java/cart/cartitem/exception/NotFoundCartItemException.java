package cart.cartitem.exception;

public class NotFoundCartItemException extends RuntimeException {

    private static final String message = "존재하지 않는 상품을 삭제하고 있습니다";

    public NotFoundCartItemException() {
        super(message);
    }
}
