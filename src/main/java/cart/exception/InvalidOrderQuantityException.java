package cart.exception;

public class InvalidOrderQuantityException extends RuntimeException {

    public InvalidOrderQuantityException() {
        super("등록된 장바구니의 수량과 일치하지 않습니다");
    }
}
