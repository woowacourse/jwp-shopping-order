package cart.order.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CanNotDeleteNotMyOrderException extends BusinessException {

  public CanNotDeleteNotMyOrderException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
