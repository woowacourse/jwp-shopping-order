package cart.factory;

import cart.domain.Amount;
import cart.domain.Coupon;

public class CouponFactory {

  public static Coupon createCoupon(final String name, final int discountedAmount, final int minAmount) {
    return new Coupon(name, new Amount(discountedAmount), new Amount(minAmount));
  }
}
