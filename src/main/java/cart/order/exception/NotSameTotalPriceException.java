package cart.order.exception;

public class NotSameTotalPriceException extends RuntimeException {

  public NotSameTotalPriceException(final String message) {
    super(message);
  }
}
