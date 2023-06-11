package cart.exception;

public enum ErrorCode {
    FORBIDDEN_MEMBER("잘못된 사용자의 시도입니다."),
    UNAUTHORIZED_MEMBER("유효하지 않은 사용자의 시도입니다."),
    INVALID_OVER_POINT("사용할 수 있는 포인트의 범위를 입력해주세요."),
    INTERNAL_SERVER_ERROR("알 수 없는 오류입니다.");

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
