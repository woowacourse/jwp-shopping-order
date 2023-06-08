package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.BusinessException;
import cart.factory.CouponFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CouponTest {

  @Test
  @DisplayName("쿠폰을 적용할 수 있다.")
  void apply() {
    //given
    final Coupon coupon = CouponFactory.createCoupon("1000원 할인", 1000, 15000);
    final Amount inputAmount = new Amount(20000);
    final Amount expectedAmount = new Amount(19000);

    //when
    final Amount discountedAmount = coupon.apply(inputAmount);

    //then
    assertThat(discountedAmount).isEqualTo(expectedAmount);
  }

  @Test
  @DisplayName("최소금액 보다 작으면 쿠폰을 적용할 수 없다.")
  void applyWhenInputAmountIsLessThanMinAmount() {
    //given
    final Coupon coupon = CouponFactory.createCoupon("1000원 할인", 1000, 15000);
    final Amount inputAmount = new Amount(10000);

    //when
    //then
    assertThatThrownBy(() -> coupon.apply(inputAmount))
        .isInstanceOf(BusinessException.class);
  }
}
