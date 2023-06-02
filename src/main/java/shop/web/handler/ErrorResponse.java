
package shop.web.handler;

public class ErrorResponse {
    private String errorMessage;

    private ErrorResponse() {
    }

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
