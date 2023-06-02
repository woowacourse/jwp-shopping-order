package cart.domain.discountpolicy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AmountCouponTest {

    @DisplayName("최소 주문 금액이 넘을 경우 정액 할인을 적용한다.")
    @Test
    void applyDiscountTest() {
        AmountCoupon amountCoupon = new AmountCoupon(10000, 1500);
        assertThat(amountCoupon.applyDiscount(11000)).isEqualTo(1500);
    }

    @DisplayName("최소 주문 금액을 못넘은 경우 쿠폰 적용 불가")
    @Test
    void doesNotApplyDiscountTest() {
        AmountCoupon amountCoupon = new AmountCoupon(10000, 1500);
        assertThat(amountCoupon.applyDiscount(9000)).isEqualTo(0);
    }

}
