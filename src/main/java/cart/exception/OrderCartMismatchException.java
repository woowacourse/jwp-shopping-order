package cart.exception;

import org.springframework.http.HttpStatus;

public class OrderCartMismatchException extends ApplicationException{
    public OrderCartMismatchException() {
        super("장바구니 내역과 주문 내역이 일치하지 않습니다. 다시 시도해 주세요.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
