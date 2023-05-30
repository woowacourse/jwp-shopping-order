package cart.exception;

public class NonExistCartItemException extends RuntimeException {

    public NonExistCartItemException() {
        super("존재하지 않는 장바구니 상품입니다.");
    }
}
