package cart.coupon.domain;

import cart.value_object.Money;

public class FixDiscountCoupon extends Coupon {

  private final Money discountPrice;

  public FixDiscountCoupon(final Long id, final String name, final Money discountPrice) {
    super(id, name);
    this.discountPrice = discountPrice;
  }

  @Override
  protected Money calculate(final Money totalPrice) {
    return totalPrice.minus(discountPrice);
  }

  public Money getDiscountPrice() {
    return discountPrice;
  }
}
