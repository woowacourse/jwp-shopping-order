package cart.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionType {

    UNAUTHORIZED_MEMBER(
            1001,
            HttpStatus.UNAUTHORIZED,
            "권한이 없는 사용자입니다."
    ),
    NO_AUTHORITY_CART_ITEM(
            1002,
            HttpStatus.FORBIDDEN,
            "다른 사용자의 장바구니를 조회할 수 없습니다."
    ),
    NOT_FOUND_MEMBER(
            1003,
            HttpStatus.NOT_FOUND,
            "존재하지 않는 사용자입니다."
    ),
    INVALID_LOGIN_INFO(
            1004,
            HttpStatus.UNAUTHORIZED,
            "잘못된 사용자 정보입니다."
    ),
    NOT_FOUND_CART_ITEM(
            2001,
            HttpStatus.NOT_FOUND,
            "존재하지 않는 장바구니입니다."
    ),
    NOT_FOUND_PRODUCT(
            3001,
            HttpStatus.NOT_FOUND,
            "존재하지 않는 상품입니다."
    ),
    NOT_FOUND_COUPON(
            4001,
            HttpStatus.NOT_FOUND,
            "존재하지 않는 쿠폰입니다."
    ),
    INVALID_DISCOUNT_VALUE(
            4003,
            HttpStatus.BAD_REQUEST,
            "잘못된 할인값입니다."
    ),
    INVALID_MIN_ORDER(
            4004,
            HttpStatus.BAD_REQUEST,
            "최소 사용 금액보다 작습니다."
    ),
    INVALID_EXPIRED_DATE(
            4005,
            HttpStatus.BAD_REQUEST,
            "만료기간이 지난 쿠폰은 사용할 수 없습니다."
    ),
    NO_AUTHORITY_COUPON(
            4006,
            HttpStatus.FORBIDDEN,
            "다른 사용자의 쿠폰을 조회할 수 없습니다."
    ),
    NOT_FOUND_ORDER(
            5001,
            HttpStatus.NOT_FOUND,
            "존재하지 않는 주문입니다."
    ),
    INCORRECT_PRICE(
            5002,
            HttpStatus.BAD_REQUEST,
            "결제 요청 금액과 다릅니다."
    ),
    NO_AUTHORITY_ORDER(
            5004,
            HttpStatus.FORBIDDEN,
            "다른 사용자의 주문을 조회할 수 없습니다."
    ),
    INVALID_VALUE(6001,
            HttpStatus.BAD_REQUEST,
            "금액은 양의 정수만 가능합니다."
    ),
    ;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    ExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
