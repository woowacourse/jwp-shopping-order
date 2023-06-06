package cart.exception;

public class CartItemNotExistException extends NoSuchDataExistException {
    private static final String CART_ITEM_NAME = "장바구니 내 상품";

    public CartItemNotExistException(final Long cartItemId) {
        super(CART_ITEM_NAME, cartItemId.toString());
    }
}
