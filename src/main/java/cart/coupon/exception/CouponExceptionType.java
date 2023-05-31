package cart.coupon.exception;

import cart.common.execption.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum CouponExceptionType implements BaseExceptionType {

    INVALID_DISCOUNT_AMOUNT(
            400,
            HttpStatus.BAD_REQUEST,
            "잘못된 할인금액입니다."
    ),
    INVALID_DISCOUNT_RATE(
            401,
            HttpStatus.BAD_REQUEST,
            "잘못된 할인율입니다."
    ),
    NOT_FOUND_COUPON(
            402,
            HttpStatus.BAD_REQUEST,
            "잘못된 쿠폰 ID 입니다."
    ),
    ;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    CouponExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int errorCode() {
        return errorCode;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
