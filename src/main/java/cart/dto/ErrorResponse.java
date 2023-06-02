package cart.dto;

import java.time.LocalDateTime;

import cart.exception.ApplicationException;

public class ErrorResponse {

    private static final String DEFAULT_MESSAGE = "처리 중 예외가 발생했습니다";

    private int status;
    private String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    private ErrorResponse() {
    }

    public ErrorResponse(int status) {
        this(status, DEFAULT_MESSAGE);
    }

    public ErrorResponse(ApplicationException exception) {
        this(exception.status().value(), exception.getMessage());
    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
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
