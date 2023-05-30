package cart.order.exception;

public class CanNotSearchNotMyOrderException extends RuntimeException {

  public CanNotSearchNotMyOrderException(final String message) {
    super(message);
  }
}
