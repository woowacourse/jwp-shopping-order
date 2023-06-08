package cart.step2.error.exception;

public class ErrorCode {

    private final int status;
    private final String message;

    public ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
