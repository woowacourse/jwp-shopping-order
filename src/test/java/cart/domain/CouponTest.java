package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CouponTest {

    @DisplayName("쿠폰을 사용해 할인한다")
    @Test
    void discount_using_coupon() {
        Coupon coupon = new Coupon(1L, 10000, DiscountPolicy.FIXED);

        assertThat(coupon.discount(new Money(12000))).isEqualTo(new Money(2000));
    }
}
