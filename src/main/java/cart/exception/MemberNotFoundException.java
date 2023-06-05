package cart.exception;

public class MemberNotFoundException extends ShoppingOrderException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
