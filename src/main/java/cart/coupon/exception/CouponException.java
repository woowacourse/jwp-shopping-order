package cart.coupon.exception;

import cart.exception.enum_exception.CustomException;
import cart.exception.enum_exception.CustomExceptionType;

public class CouponException extends CustomException {

  private final CouponExceptionType couponExceptionType;

  public CouponException(final CouponExceptionType couponExceptionType) {
    super(couponExceptionType.errorMessage());
    this.couponExceptionType = couponExceptionType;
  }

  @Override
  public CustomExceptionType getExceptionType() {
    return couponExceptionType;
  }
}
