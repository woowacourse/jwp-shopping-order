package cart.order.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CanNotChangeNotMyOrderException extends BusinessException {

  public CanNotChangeNotMyOrderException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
