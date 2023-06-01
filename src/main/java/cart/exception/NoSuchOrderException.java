package cart.exception;

public class NoSuchOrderException extends IllegalArgumentException {
    public NoSuchOrderException() {
        super("존재하지 않는 주문입니다.");
    }
}
