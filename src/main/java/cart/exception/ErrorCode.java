package cart.exception;

public enum ErrorCode {

    NOT_AUTHENTICATION_MEMBER("인증되지 않은 사용자입니다."),
    NOT_AUTHORIZATION_MEMBER("권한이 없는 사용자입니다."),
    DUPLICATED_NAME("중복된 아이디입니다. 다른 아이디를 입력해주세요."),
    INVALID_PRODUCT_ID("유효하지 않은 상품 ID 입니다."),
    INTERNAL_SERVER_ERROR("예상치 못한 서버 오류가 발생하였습니다.");

    private final String errorMessage;

    ErrorCode(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
