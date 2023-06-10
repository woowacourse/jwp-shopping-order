package cart.dto;

import cart.exception.ApplicationException;

import java.time.LocalDateTime;

public class ErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;

    private ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorResponse(ApplicationException exception) {
        this(exception.status().value(), exception.getMessage(), LocalDateTime.now());
    }

    public ErrorResponse(int status, String message) {
        this(status, message, LocalDateTime.now());
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
