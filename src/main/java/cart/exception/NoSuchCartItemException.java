package cart.exception;

public final class NoSuchCartItemException extends RuntimeException {

    public NoSuchCartItemException() {
        super("찾을 수 없는 장바구니입니다.");
    }
}
