package cart.exception;

public class CartItemNotFoundException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 장바구니 상품이 포함되어 있습니다.";

    public CartItemNotFoundException() {
        super(MESSAGE);
    }

}
