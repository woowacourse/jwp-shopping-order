package cart.exception;

public class InvalidCartItemOwnerException extends CartException {

    public InvalidCartItemOwnerException() {
        super("장바구니의 소유자가 아닙니다.");
    }
}
