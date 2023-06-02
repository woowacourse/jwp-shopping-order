
package shop.presentation;

public class ErrorResponse {
    private String message;

    private ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
