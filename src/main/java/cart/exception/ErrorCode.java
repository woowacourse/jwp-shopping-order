package cart.exception;

public enum ErrorCode {
    FORBIDDEN_MEMBER("잘못된 사용자의 시도입니다."),
    UNAUTHORIZED_MEMBER("유효하지 않은 사용자의 시도입니다.");


    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
