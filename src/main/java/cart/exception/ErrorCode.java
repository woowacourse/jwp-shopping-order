package cart.exception;

public enum ErrorCode {
    NOT_ENOUGH_STOCK(1), NOT_ENOUGH_POINT(2), ILLEGAL_MEMBER(3),
    ;

    private final int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
