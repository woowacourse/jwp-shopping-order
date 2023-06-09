package cart.member_coupon.exception;

import cart.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundMemberCouponException extends BusinessException {

  public NotFoundMemberCouponException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
