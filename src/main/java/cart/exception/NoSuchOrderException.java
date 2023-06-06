package cart.exception;

public final class NoSuchOrderException extends RuntimeException {

    public NoSuchOrderException() {
        super("찾을 수 없는 주문입니다.");
    }
}
