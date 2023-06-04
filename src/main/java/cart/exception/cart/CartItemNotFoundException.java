package cart.exception.cart;

public class CartItemNotFoundException extends CartItemException{

    private static final String CART_ITEM_NOT_FOUND_EXCEPTION_MESSAGE = "존재하지 않는 장바구니 상품입니다.";

    public CartItemNotFoundException() {
        super(CART_ITEM_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
