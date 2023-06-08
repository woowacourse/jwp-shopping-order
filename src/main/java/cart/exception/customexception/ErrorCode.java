package cart.exception.customexception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    CART_ITEM_QUANTITY_EXCESS(HttpStatus.CONFLICT, 1, "상품의 재고가 부족합니다."),
    POINT_NOT_ENOUGH(HttpStatus.CONFLICT, 2, "포인트가 부족합니다."),
    ORDER_TOTAL_PRICE_UNMATCHED(HttpStatus.BAD_REQUEST, 3, "주문 가격과 상품 가격의 합이 맞지 않습니다. 확인 후 재주문 부탁드립니다."),
    ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, 4, "해당 주문을 찾을 수 없습니다. 다시 조회해보십시오."),
    POINT_EXCEED_TOTAL_PRICE(HttpStatus.BAD_REQUEST, 5, "상품의 총 가격보다 많은 포인트를 사용할 수 없습니다. 사용 포인트를 적게 입력해주십시오."),
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, 6, "존재하지 않는 상품입니다. 판매자에게 문의하세요."),
    CART_ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, 7, "장바구니에 아이템이 존재하지 않습니다. 확인 후 재주문 부탁드립니다."),
    CART_ITEM_DUPLICATED(HttpStatus.BAD_REQUEST, 8, "이미 장바구니에 존재하는 상품입니다. 동일한 상품을 추가할 수 없습니다."),
    ILLEGAL_MEMBER(HttpStatus.FORBIDDEN, 9, "접근할 수 없는 유저입니다."),
    AUTHENTICATION(HttpStatus.UNAUTHORIZED, 11, "인증에 실패하였습니다. 회원정보를 다시 입력해주세요."),
    PRE_AUTHENTICATION(HttpStatus.BAD_REQUEST, 12, "인증 절차를 진행할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String message;

    ErrorCode(HttpStatus httpStatus, int errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}