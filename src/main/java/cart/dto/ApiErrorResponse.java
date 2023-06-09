package cart.dto;

public class ApiErrorResponse {
    private final String errorMessage;

    public ApiErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static ApiErrorResponse from(Exception exception) {
        return new ApiErrorResponse(exception.getMessage());
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
