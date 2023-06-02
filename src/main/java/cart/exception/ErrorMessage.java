package cart.exception;

import org.springframework.http.HttpStatus;

public enum ErrorMessage {
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "장바구니의 상품 수량은 0 이상이어야 합니다."),
    INVALID_AUTHORIZATION_INFORMATION(HttpStatus.BAD_REQUEST, "이메일과 비밀번호를 모두 입력해야 합니다."),
    INVALID_POINT(HttpStatus.BAD_REQUEST, "포인트는 0 이상이어야 합니다."),
    INVALID_DELIVERY_FEE(HttpStatus.BAD_REQUEST, "배송비는 0 이상이어야 합니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "가격은 0 이상이어야 합니다."),
    INVALID_MEMBER_POINT_LESS_THAN_USED_POINT(HttpStatus.BAD_REQUEST, "사용한 포인트는 사용자의 포인트보다 적어야 합니다."),
    INVALID_POINT_MORE_THAN_PRICE(HttpStatus.BAD_REQUEST, "사용할 포인트는 주문 금액보다 클 수 없습니다."),
    INVALID_(HttpStatus.BAD_REQUEST, "사용할 포인트는 주문 금액보다 클 수 없습니다."),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "비밀번호가 다릅니다."),
    INVALID_CART_ITEM_OWNER(HttpStatus.FORBIDDEN, "사용자의 장바구니 상품이 아닙니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    NOT_FOUND_CART_ITEM(HttpStatus.NOT_FOUND, "장바구니에서 상품을 찾을 수 없습니다."),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "주문 내역을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생했습니다."),
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
