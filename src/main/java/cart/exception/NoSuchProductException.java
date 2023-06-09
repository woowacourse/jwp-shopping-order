package cart.exception;

public class NoSuchProductException extends IllegalArgumentException {
    public NoSuchProductException() {
        super("존재하지 않는 상품입니다.");
    }
}
