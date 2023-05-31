package cart.exception;

public class CartItemNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "id: %s 에 해당하는 장바구니 품목을 찾을 수 없습니다.";

    public CartItemNotFoundException(final Long id) {
        super(String.format(MESSAGE, id));
    }
}
