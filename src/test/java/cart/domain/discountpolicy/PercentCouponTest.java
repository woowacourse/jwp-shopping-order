package cart.domain.discountpolicy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PercentCouponTest {

    @DisplayName("정률 할인을 적용한다.")
    @Test
    void discountPercentTest() {
        PercentCoupon percentCoupon = new PercentCoupon(1000, 6);
        int paymentPrice = percentCoupon.applyDiscount(9000);
        Assertions.assertThat(paymentPrice).isEqualTo(540);
    }

    @DisplayName("최소 주문 금액을 못넘기면 쿠폰 적용 불가.")
    @Test
    void doesNotDiscountPercentTest() {
        PercentCoupon percentCoupon = new PercentCoupon(10000, 6);
        int paymentPrice = percentCoupon.applyDiscount(9000);
        Assertions.assertThat(paymentPrice).isEqualTo(0);
    }

}
