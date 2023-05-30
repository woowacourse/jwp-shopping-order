package cart.order.exception;

public class NotFoundOrderException extends RuntimeException {

  public NotFoundOrderException(final String message) {
    super(message);
  }
}
