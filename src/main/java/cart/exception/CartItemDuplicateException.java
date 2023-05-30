package cart.exception;

import org.springframework.http.HttpStatus;

public class CartItemDuplicateException extends ApplicationException {

    public CartItemDuplicateException() {
        super("이미 장바구니에 존재하는 상품입니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
