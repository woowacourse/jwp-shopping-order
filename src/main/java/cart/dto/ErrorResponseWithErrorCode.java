package cart.dto;

public class ErrorResponseWithErrorCode {

    private final int errorCode;
    private final String message;

    public ErrorResponseWithErrorCode(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
