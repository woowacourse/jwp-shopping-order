package cart.exception;

public final class NoSuchProductException extends RuntimeException {

    public NoSuchProductException() {
        super("찾을 수 없는 물품입니다.");
    }
}
