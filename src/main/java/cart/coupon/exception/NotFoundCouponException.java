package cart.coupon.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundCouponException extends BusinessException {

  public NotFoundCouponException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
