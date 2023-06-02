package cart.exception;

import org.springframework.http.HttpStatus;

public class OrderUnauthorizedException extends ApplicationException{
    public OrderUnauthorizedException() {
        super("해당 주문을 볼 수 있는 권한이 없습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
