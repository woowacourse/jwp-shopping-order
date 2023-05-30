package cart.exception.notfound;

public class CartItemNotFoundException extends NotFoundException {

    private static final String message = "해당 장바구니 상품을 찾을 수 없습니다. 입력한 장바구니 상품 id: %d";

    public CartItemNotFoundException() {
        super("해당 장바구니 상품을 찾을 수 없습니다.");
    }

    public CartItemNotFoundException(final Long cartItemId) {
        super(String.format(message, cartItemId));
    }
}
