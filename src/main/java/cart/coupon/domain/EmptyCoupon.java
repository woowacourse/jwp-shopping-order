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
}
