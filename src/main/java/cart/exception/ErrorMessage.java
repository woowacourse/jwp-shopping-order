package cart.exception;

import org.springframework.http.HttpStatus;

public enum ErrorMessage {
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
    NOT_FOUND_CART_ITEM(HttpStatus.BAD_REQUEST, "장바구니에서 상품을 찾을 수 없습니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "장바구니의 상품 수량은 0 이상이어야 합니다."),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "비밀번호가 다릅니다."),
    ;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorMessage(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
