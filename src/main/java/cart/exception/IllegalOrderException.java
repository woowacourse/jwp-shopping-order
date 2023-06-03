package cart.exception;

public class IllegalOrderException extends RuntimeException {

    public IllegalOrderException() {
        super("정상적인 주문이 아닙니다.");
    }
}
