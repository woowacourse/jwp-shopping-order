package cart.exception.notFound;

public class CartItemNotFountException extends NotFoundException{
    private static final String MESSAGE = "해당 cartItem이 존재하지 않습니다.";

    public CartItemNotFountException() {
        super(MESSAGE);
    }
}
