package cart.exception.enum_exception;

public abstract class CustomException extends RuntimeException {

  protected CustomException(final String message) {
    super(message);
  }

  public abstract CustomExceptionType getExceptionType();
}
