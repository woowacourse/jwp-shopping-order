package cart.coupon.domain;

import cart.value_object.Money;

public class EmptyCoupon extends Coupon {

  public EmptyCoupon() {
    super(null, null);
  }

  @Override
  protected Money calculate(final Money totalPrice) {
    return totalPrice;
  }

  @Override
  protected Money discountPrice() {
    return Money.ZERO;
  }

  @Override
  public String getName() {
    return NOT_USE_COUPON_NAME;
  }

  @Override
  public Long getId() {
    return NOT_USE_ID;
  }
}
