package cart.cart_item.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CanNotRemoveNotMyCartItemException extends BusinessException {

  public CanNotRemoveNotMyCartItemException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
