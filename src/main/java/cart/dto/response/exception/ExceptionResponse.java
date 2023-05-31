package cart.dto.response.exception;

public class ExceptionResponse {

    private String errorMessage;

    ExceptionResponse(){
    }

    public ExceptionResponse(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
