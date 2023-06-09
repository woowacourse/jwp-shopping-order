package cart.dto.error;

import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorResponse {
    private final String message;
    private final int status;
    private final List<FieldError> errors;

    public ErrorResponse(String message, int status, List<FieldError> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
