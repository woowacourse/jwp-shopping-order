package cart.exception;

public class AuthenticationException extends RuntimeException{
    private final ExceptionType exceptionType;

    public AuthenticationException(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}
