package cart.cart_item.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotAllCartItemInCartException extends BusinessException {

  public NotAllCartItemInCartException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
