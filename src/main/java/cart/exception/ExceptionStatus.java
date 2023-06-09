package cart.exception;

import java.util.Arrays;
import org.springframework.http.HttpStatus;

public enum ExceptionStatus {

    INTERNAL_SERVER_ERROR(
            999,
            HttpStatus.INTERNAL_SERVER_ERROR,
            ExceptionType.INTERNAL_SERVER_ERROR
    ),

    NO_AUTHORITY_CART_ITEM(
            1002,
            HttpStatus.FORBIDDEN,
            ExceptionType.NO_AUTHORITY_CART_ITEM
    ),
    NOT_FOUND_MEMBER(
            1003,
            HttpStatus.NOT_FOUND,
            ExceptionType.NOT_FOUND_MEMBER
    ),
    INVALID_LOGIN_INFO(
            1004,
            HttpStatus.UNAUTHORIZED,
            ExceptionType.INVALID_LOGIN_INFO
    ),

    NOT_FOUND_CART_ITEM(
            2001,
            HttpStatus.NOT_FOUND,
            ExceptionType.NOT_FOUND_CART_ITEM
    ),

    NOT_FOUND_PRODUCT(
            3001,
            HttpStatus.NOT_FOUND,
            ExceptionType.NOT_FOUND_PRODUCT
    ),

    NOT_FOUND_COUPON(
            4001,
            HttpStatus.NOT_FOUND,
            ExceptionType.NOT_FOUND_COUPON
    ),
    INVALID_DISCOUNT_VALUE(
            4003,
            HttpStatus.BAD_REQUEST,
            ExceptionType.INVALID_DISCOUNT_VALUE
    ),
    INVALID_MIN_ORDER(
            4004,
            HttpStatus.BAD_REQUEST,
            ExceptionType.INVALID_MIN_ORDER
    ),
    INVALID_EXPIRED_DATE(
            4005,
            HttpStatus.BAD_REQUEST,
            ExceptionType.INVALID_EXPIRED_DATE
    ),
    NO_AUTHORITY_COUPON(
            4006,
            HttpStatus.FORBIDDEN,
            ExceptionType.NO_AUTHORITY_COUPON
    ),

    NOT_FOUND_ORDER(
            5001,
            HttpStatus.NOT_FOUND,
            ExceptionType.NOT_FOUND_ORDER
    ),
    INCORRECT_PRICE(
            5002,
            HttpStatus.BAD_REQUEST,
            ExceptionType.INCORRECT_PRICE
    ),
    NO_AUTHORITY_ORDER(
            5004,
            HttpStatus.FORBIDDEN,
            ExceptionType.NO_AUTHORITY_ORDER
    ),

    INVALID_VALUE(
            6001,
            HttpStatus.BAD_REQUEST,
            ExceptionType.INVALID_VALUE
    ),
    ;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final ExceptionType exceptionType;

    ExceptionStatus(int errorCode, HttpStatus httpStatus, ExceptionType exceptionType) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.exceptionType = exceptionType;
    }

    public static ExceptionStatus findByType(ExceptionType exceptionType) {
        return Arrays.stream(values())
                .filter(exceptionResponse -> exceptionResponse.exceptionType.equals(exceptionType))
                .findFirst()
                .orElse(INTERNAL_SERVER_ERROR);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
