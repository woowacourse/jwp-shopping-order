package cart.exception.order;

public class CartItemQuantityDoesNotMatchException extends OrderException {
    private static final String MESSAGE = "cartItem의 quantity가 일치하지 않습니다. 다시 확인해주세요.";

    public CartItemQuantityDoesNotMatchException() {
        super(MESSAGE);
    }
}
