package cart.exception.notfoundexception;

public class CartItemNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 장바구니 상품이 포함되어 있습니다.";

    public CartItemNotFoundException() {
        super(MESSAGE);
    }

}
