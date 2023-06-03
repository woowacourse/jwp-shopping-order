package cart.dto.response;

import cart.exception.ExceptionType;

public class ExceptionResponse {
    private final ExceptionType exceptionType;

    public ExceptionResponse(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}
