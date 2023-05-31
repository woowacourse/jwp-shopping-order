package cart.exception.forbidden;

import cart.exception.StoreException;

public class ForbiddenException extends StoreException {

    public ForbiddenException() {
        super("해당 자원에 대한 접근 권한이 없습니다.");
    }
}
