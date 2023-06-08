package cart.step2.error.exception;

public class ShoppingOrderException extends RuntimeException {

    private final ErrorCode errorCode;

    public ShoppingOrderException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
