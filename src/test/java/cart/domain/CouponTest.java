package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.vo.Amount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CouponTest {

    @Test
    @DisplayName("총 상품 금액에 쿠폰 할인 금액을 적용할 수 있다.")
    void testCalculateDiscountedAmount() {
        // given
        final Coupon coupon = new Coupon("1000원 할인 쿠폰", Amount.of(1_000), Amount.of(0));
        final int totalProductAmount = 10_000;

        // when & then
        assertThat(coupon.calculateDiscountedAmount(totalProductAmount)).isEqualTo(9_000);
    }

    @Test
    @DisplayName("총 상품 금액이 쿠폰 할인 금액 이하면 0원으로 계산한다.")
    void testCalculateDiscountedAmountWhenTotalAmountLessThanDiscountAmount() {
        // given
        final Coupon coupon = new Coupon("10000원 할인 쿠폰", Amount.of(10_000), Amount.of(0));
        final int totalProductAmount = 5_000;

        // when & then
        assertThat(coupon.calculateDiscountedAmount(totalProductAmount)).isEqualTo(0);
    }
}
