package cart.exception;

import org.springframework.http.HttpStatus;

public enum ErrorStatus {

    //Authentication code: 1xxx
    AUTHENTICATION_INVALID(HttpStatus.UNAUTHORIZED, "인증 정보가 존재하지 않습니다.", 1000),

    //Member code: 2xxx
    MEMBER_ILLEGAL_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없는 회원입니다.", 2000),

    //Point code: 3xxx
    POINT_NOT_NEGATIVE(HttpStatus.BAD_REQUEST, "포인트는 음수가 될 수 없습니다.", 3000),
    POINT_NOT_ENOUGH(HttpStatus.CONFLICT, "회원님의 현재 포인트가 사용하려는 포인트보다 작습니다.", 3001),
    POINT_OVER_PRICE(HttpStatus.BAD_REQUEST, "사용하려는 포인트가 결제 금액을 초과합니다.", 3002),

    //Payment code: 4xxx
    PAYMENT_PRICE_INVALID(HttpStatus.BAD_REQUEST, "결제 요청 금액이 실제 상품 가격과 다릅니다.", 4000);

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;

    ErrorStatus(final HttpStatus httpStatus, final String message, final int code) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
