package cart.exception;

import org.springframework.http.HttpStatus;

public class OrderCartMismatchException extends ApplicationException{
    public OrderCartMismatchException() {
        super("장바구니 내용이 변경되었습니다. 장바구니를 삭제한 후, 다시 추가해주세요.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
