package cart.order.exception;

import cart.common.execption.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum OrderExceptionType implements BaseExceptionType {

    NOT_FOUND_ORDER(
            100,
            HttpStatus.BAD_REQUEST,
            "잘못된 주문 ID가 포함되어 있습니다."
    ),
    NOT_FOUND_ORDER_ITEM(
            101,
            HttpStatus.BAD_REQUEST,
            "잘못된 주문 상품 ID가 포함되어 있습니다."
    ),
    INVALID_ORDER_ITEM_PRODUCT_QUANTITY(
            102,
            HttpStatus.BAD_REQUEST,
            "주문 상품의 상품 수량은 양수여야 합니다."
    ),
    NON_EXIST_ORDER_ITEM(
            103,
            HttpStatus.BAD_REQUEST,
            "주문 상품이 존재하지 않습니다."
    ),
    ;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    OrderExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
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
