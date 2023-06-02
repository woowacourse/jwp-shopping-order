package cart.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends ApplicationException{
    public OrderNotFoundException() {
        super("주문 정보를 확인할 수 없습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
