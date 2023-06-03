package cart.exception;

public enum ErrorCode {
    /**
     * PRODUCT
     */
    PRODUCT_NOT_FOUND("상품이 존재하지 않습니다."),
    PRODUCT_NAME_LENGTH("상품 이름의 길이는 1 ~ 20자까지 가능합니다."),
    PRODUCT_PRICE_RANGE("상품 가격은 1 ~ 10,000,000원까지 가능합니다."),
    PRODUCT_DELETED("현재 판매 중이지 않은 상품은 주문할 수 없습니다."),

    /**
     * MEMBER
     */
    MEMBER_NAME_LENGTH("사용자 이름의 길이는 4 ~ 10자까지 가능합니다"),
    MEMBER_PASSWORD_LENGTH("비밀번호 길이는 4 ~ 10자까지 가능합니다."),
    MEMBER_NOT_FOUND("사용자 정보를 찾을 수 없습니다."),
    MEMBER_DUPLICATE_NAME("이미 등록된 사용자 이름입니다."),
    MEMBER_PASSWORD_INVALID("비밀번호가 일치하지 않습니다."),

    /**
     * COUPON
     */
    COUPON_NAME_LENGTH("쿠폰 이름의 길이는 4 ~ 10자까지 가능합니다."),
    COUPON_PERIOD_RANGE("쿠폰 기간은 1 ~ 365일까지 가능합니다."),
    COUPON_DISCOUNT_RATE_RANGE("쿠폰 할인율은 5 ~ 90%까지 가능합니다."),
    COUPON_DUPLICATE("이미 존재하는 쿠폰 정보입니다."),
    COUPON_NOT_FOUND("쿠폰 정보를 찾을 수 없습니다."),
    COUPON_EXPIRED("만료된 쿠폰입니다."),
    COUPON_ALREADY_EXIST("이미 발급된 쿠폰입니다."),
    COUPON_ALREADY_USED("이미 사용한 쿠폰입니다."),
    COUPON_NOT_FIRST_ORDER("첫 주문이 아닙니다."),

    /**
     * CART
     */
    CART_NOT_FOUND("장바구니 정보를 찾을 수 없습니다."),
    CART_ALREADY_ADD("장바구니에 해당 상품이 이미 등록되어 있습니다."),

    /**
     * ORDER
     */
    ORDER_NOT_FOUND("존재하지 않는 주문 정보입니다."),
    ORDER_INVALID_PRODUCTS("장바구니에 담기지 않은 상품은 주문할 수 없습니다."),
    ORDER_QUANTITY_EXCEED("상품은 최대 100,000개까지 주문할 수 있습니다."),
    ORDER_CANNOT_CANCEL("주문 취소 기간이 지나 취소가 불가능합니다."),

    /**
     * VALIDATION ERROR
     */
    INVALID_REQUEST(""),

    /**
     * HTTP STATUS CODE - 401
     */
    UNAUTHORIZED("인증되지 않은 사용자입니다."),

    /**
     * HTTP STATUS CODE - 403
     */
    FORBIDDEN("권한이 없습니다."),

    /**
     * HTTP STATUS CODE - 500
     */
    DB_UPDATE_ERROR("DB 업데이트가 정상적으로 진행되지 않았습니다."),
    DB_DELETE_ERROR("DB 삭제가 정상적으로 진행되지 않았습니다."),
    INTERNAL_SERVER_ERROR("서버에서 예기치 못한 오류가 발생하였습니다.");

    private final String message;

    ErrorCode(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
