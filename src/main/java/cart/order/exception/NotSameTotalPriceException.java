package cart.order.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotSameTotalPriceException extends BusinessException {

  public NotSameTotalPriceException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
