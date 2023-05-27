package cart.exception;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException() {
        super("장바구니에 존재하지 않는 상품입니다.");
    }
}
