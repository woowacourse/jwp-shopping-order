package cart.exception;

public class CartItemNotFoundException extends NotFoundException {

    public CartItemNotFoundException() {
        super("장바구니 상품을 찾을 수 없습니다.");
    }
}
