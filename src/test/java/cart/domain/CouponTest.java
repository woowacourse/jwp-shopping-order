package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CouponTest {

  @Test
  void apply() {
    final Coupon coupon = new Coupon(new Amount(1000), new Amount(15000));
    final Amount inputAmount = new Amount(20000);
    final Amount expectedAmount = new Amount(19000);

    final Amount discountedAmount = coupon.apply(inputAmount);

    assertThat(discountedAmount).isEqualTo(expectedAmount);
  }
}
