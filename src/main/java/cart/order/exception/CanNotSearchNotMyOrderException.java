package cart.order.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CanNotSearchNotMyOrderException extends BusinessException {

  public CanNotSearchNotMyOrderException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
