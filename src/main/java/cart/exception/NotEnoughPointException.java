package cart.exception;

public final class NotEnoughPointException extends RuntimeException {

    public NotEnoughPointException() {
        super("포인트가 부족합니다.");
    }
}
