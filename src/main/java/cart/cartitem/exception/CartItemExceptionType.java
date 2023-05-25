package cart.cartitem.exception;

import cart.common.execption.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum CartItemExceptionType implements BaseExceptionType {

    NOT_FOUND_CART_ITEM(
            100,
            HttpStatus.BAD_REQUEST,
            "잘못된 장바구니 상품 ID가 포함되어 있습니다."
    ),
    NO_AUTHORITY_UPDATE_ITEM(
            101,
            HttpStatus.FORBIDDEN,
            "상품을 수정할 권한이 없습니다."
    ),
    ;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    CartItemExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
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
