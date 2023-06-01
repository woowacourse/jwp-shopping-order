package cart.exception;

public class NonExistOrderException extends RuntimeException {

    public NonExistOrderException() {
        super("존재하지 않는 주문입니다.");
    }
}
