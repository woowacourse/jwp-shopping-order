package cart.order.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CanNotDiscountPriceMoreThanTotalPrice extends BusinessException {

  public CanNotDiscountPriceMoreThanTotalPrice(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
