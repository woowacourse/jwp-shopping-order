package cart.order.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CanNotDiscountPriceMoreThanTotalPriceException extends BusinessException {

  public CanNotDiscountPriceMoreThanTotalPriceException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
