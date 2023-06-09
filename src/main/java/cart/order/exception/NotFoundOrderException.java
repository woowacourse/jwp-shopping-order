package cart.order.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundOrderException extends BusinessException {

  public NotFoundOrderException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
