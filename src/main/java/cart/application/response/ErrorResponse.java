package cart.application.response;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final String status;
    private final String message;
    private final LocalDateTime createdAt;

    private ErrorResponse(String status, String message, LocalDateTime createdAt) {
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static ErrorResponse from(String status, String message, LocalDateTime createdAt) {
        return new ErrorResponse(status, message, createdAt);
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
