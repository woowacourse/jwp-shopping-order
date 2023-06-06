package cart.exception;

public class InvalidOrderCheckedException extends RuntimeException {

    public InvalidOrderCheckedException() {
        super("등록된 장바구니의 체크 여부와 일치하지 않습니다");
    }
}
