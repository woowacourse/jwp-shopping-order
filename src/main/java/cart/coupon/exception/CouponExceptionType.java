package cart.coupon.exception;

import cart.exception.enum_exception.CustomExceptionType;
import org.springframework.http.HttpStatus;

public enum CouponExceptionType implements CustomExceptionType {

  NOT_FOUND_COUPON(
      "해당 쿠폰은 존재하지 않습니다",
      HttpStatus.BAD_REQUEST
  ),

  NOT_FOUND_MEMBER_COUPON(
      "해당 멤버는 현재 쿠폰을 가지고 있지 않습니다.",
      HttpStatus.BAD_REQUEST
  )
  ;

  private final String message;
  private final HttpStatus httpStatus;

  CouponExceptionType(final String message, final HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

  @Override
  public HttpStatus httpStatus() {
    return httpStatus;
  }

  @Override
  public String errorMessage() {
    return message;
  }
}
