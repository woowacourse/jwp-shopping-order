package cart.exception.notfound;

public class CartItemNotFoundException extends NotFoundException {

    public CartItemNotFoundException(Long id) {
        super("해당 장바구니 상품이 존재하지 않습니다. 요청 장바구니 상품 ID: " + id);
    }

    public CartItemNotFoundException(String message) {
        super(message);
    }
}
