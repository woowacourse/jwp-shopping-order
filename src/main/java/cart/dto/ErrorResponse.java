package cart.dto;

import java.util.Objects;

public class ErrorResponse {
    private String errorType;

    public ErrorResponse(final String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ErrorResponse that = (ErrorResponse) o;
        return Objects.equals(errorType, that.errorType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorType);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errorType='" + errorType + '\'' +
                '}';
    }
}
