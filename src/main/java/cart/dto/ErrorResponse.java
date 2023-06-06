package cart.dto;

public class ErrorResponse {

    private final int errorCode;

    public ErrorResponse(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
