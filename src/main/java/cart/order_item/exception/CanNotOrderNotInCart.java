package cart.order_item.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CanNotOrderNotInCart extends BusinessException {

  public CanNotOrderNotInCart(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
