package cart.exception;

public enum ExceptionType {

    INTERNAL_SERVER_ERROR("서버 오류입니다."),

    NO_AUTHORITY_CART_ITEM("다른 사용자의 장바구니를 조회할 수 없습니다."),
    NOT_FOUND_MEMBER("존재하지 않는 사용자입니다."),
    INVALID_LOGIN_INFO("잘못된 사용자 정보입니다."),

    NOT_FOUND_CART_ITEM("존재하지 않는 장바구니입니다."),

    NOT_FOUND_PRODUCT("존재하지 않는 상품입니다."),

    NOT_FOUND_COUPON("존재하지 않는 쿠폰입니다."),
    INVALID_DISCOUNT_VALUE("잘못된 할인값입니다."),
    INVALID_MIN_ORDER("최소 사용 금액보다 작습니다."),
    INVALID_EXPIRED_DATE("만료기간이 지난 쿠폰은 사용할 수 없습니다."),
    NO_AUTHORITY_COUPON("다른 사용자의 쿠폰을 조회할 수 없습니다."),

    NOT_FOUND_ORDER("존재하지 않는 주문입니다."),
    INCORRECT_PRICE("결제 요청 금액과 다릅니다."),
    NO_AUTHORITY_ORDER("다른 사용자의 주문을 조회할 수 없습니다."),

    INVALID_VALUE("금액은 양의 정수만 가능합니다."),
    ;

    private final String errorMessage;

    ExceptionType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

