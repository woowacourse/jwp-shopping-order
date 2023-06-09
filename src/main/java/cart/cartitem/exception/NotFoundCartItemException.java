package cart.cartitem.exception;

public class NotFoundCartItemException extends RuntimeException {

    private static final String message = "존재하지 않는 장바구니 상품입니다";

    public NotFoundCartItemException() {
        super(message);
    }
}
