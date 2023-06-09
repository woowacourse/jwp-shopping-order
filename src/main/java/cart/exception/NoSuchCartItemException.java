package cart.exception;

public class NoSuchCartItemException extends IllegalArgumentException {
    public NoSuchCartItemException() {
        super("존재하지 않는 장바구니 아이템입니다.");
    }
}
