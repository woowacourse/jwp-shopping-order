package cart.exception;

public class BadRequestException extends RuntimeException{
    private final ExceptionType exceptionType;

    public BadRequestException(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}
