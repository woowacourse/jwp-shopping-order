package cart.exception;

import org.springframework.http.HttpStatus;

public class ItemOwnerNotMatchException extends ApplicationException {

    public ItemOwnerNotMatchException() {
        super("장바구니 상품이 해당하는 멤버의 것이 아닙니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
