package cart.order_item.exception;

public class CanNotOrderNotInCart extends RuntimeException {

  public CanNotOrderNotInCart(final String message) {
    super(message);
  }
}
