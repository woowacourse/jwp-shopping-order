package cart.cart_item.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CanNotRemoveCartItemsMoreThanSavedCartItems extends BusinessException {

  public CanNotRemoveCartItemsMoreThanSavedCartItems(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
