package cart.dto;

public class ErrorResponse {
    private String error;

    public ErrorResponse() {
    }

    public ErrorResponse(final String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
