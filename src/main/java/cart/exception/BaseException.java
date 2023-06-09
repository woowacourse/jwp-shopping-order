package cart.exception;

public abstract class BaseException extends RuntimeException {

    public abstract int getErrorCode();
    public abstract String getErrorMessage();
}
