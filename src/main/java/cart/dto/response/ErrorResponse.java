package cart.dto.response;

import cart.exception.ApplicationException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;

    private ErrorResponse() {
    }

    private ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(HttpStatus status, String message) {
        this(status.value(), message);
    }

    public ErrorResponse(ApplicationException exception) {
        this(exception.status().value(), exception.getMessage());
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
