package cart.cart_item.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotExistCartItemInCart extends BusinessException {

  public NotExistCartItemInCart(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
