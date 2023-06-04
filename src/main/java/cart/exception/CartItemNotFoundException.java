package cart.exception;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(final long productId) {
        super("장바구니에 존재하지 않는 상품입니다. 상품 번호 : " + productId);
    }
}
