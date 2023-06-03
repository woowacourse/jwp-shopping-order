package cart.exception;

public class ForbiddenException extends RuntimeException {
    private final ExceptionType exceptionType;

    public ForbiddenException(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}
