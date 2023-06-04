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
    throw new UnsupportedOperationException();
  }

  @Override
  public Long getId() {
    throw new UnsupportedOperationException();
  }
}
