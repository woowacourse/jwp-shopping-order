package cart.exception;

public enum ErrorCode {

    NOT_AUTHENTICATION_MEMBER("인증되지 않은 사용자입니다."),
    NOT_AUTHORIZATION_MEMBER("권한이 없는 사용자입니다."),
    DUPLICATED_NAME("중복된 아이디입니다. 다른 아이디를 입력해주세요."),
    INVALID_PRODUCT_NAME_LENGTH("상품 이름은 1글자 이상 20글자 이하로 입력해주세요."),
    INVALID_PRODUCT_PRICE_SIZE("상품 가격은 1원 이상 10,000,000원 이하로 입력해주세요."),
    INVALID_MEMBER_NAME_LENGTH("아이디는 4글자 이상 10글자 이하로 입력해주세요."),
    INVALID_PASSWORD_LENGTH("비밀번호는 4글자 이상 10글자 이하로 입력해주세요."),
    INVALID_COUPON_NAME_LENGTH("쿠폰 이름은 1글자 이상 50글자 이하로 입력해주세요."),
    INVALID_COUPON_DISCOUNT_RATE("쿠폰 할인율은 5% 이상 90% 이하로 입력해주세요"),
    INVALID_COUPON_PERIOD_LENGTH("쿠폰 기간은 1일 이상 365일 이하로 입력해주세요."),
    INVALID_COUPON_EXPIRATION_DATE("쿠폰 만료 날짜는 현재 시간 이후로 입력해주세요."),
    INVALID_ITEM_QUANTITY_SIZE("상품 수량은 1개 이상 1000개 이하로 입력해주세요."),

    INVALID_MEMBER_ID("유효하지 않은 멤버 ID 입니다."),
    INVALID_PRODUCT_ID("유효하지 않은 상품 ID 입니다."),
    INVALID_CART_ID("유효하지 않은 카트 ID 입니다."),
    INVALID_COUPON_ID("유효하지 않은 쿠폰 ID 입니다."),
    INVALID_ORDER_ID("유효하지 않은 주문번호 ID 입니다."),
    ALREADY_USED_COUPON("이미 사용한 쿠폰입니다."),
    ALREADY_ISSUED_COUPON("이미 발급받은 쿠폰입니다."),
    HTTP_REQUEST_EXCEPTION("HTTP 요청에 실패하였습니다."),
    INTERNAL_SERVER_ERROR("예상치 못한 서버 오류가 발생하였습니다.");

    private final String errorMessage;

    ErrorCode(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
