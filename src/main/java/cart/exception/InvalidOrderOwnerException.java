package cart.exception;

public class InvalidOrderOwnerException extends CartException {
    public InvalidOrderOwnerException() {
        super("주문의 소유자가 아닙니다.");
    }
}
