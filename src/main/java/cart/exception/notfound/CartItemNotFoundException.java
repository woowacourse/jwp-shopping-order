package cart.exception.notfound;

public class CartItemNotFoundException extends NotFoundException {
    public CartItemNotFoundException() {
        super("찾을 수 없는 장바구니 상품입니다.");
    }
}
