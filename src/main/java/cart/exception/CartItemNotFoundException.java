package cart.exception;

public class CartItemNotFoundException extends CartItemException {
    public CartItemNotFoundException(final Long id) {
        super("해당 장바구니 상품을 찾을 수 없습니다 : " + id);
    }
}
