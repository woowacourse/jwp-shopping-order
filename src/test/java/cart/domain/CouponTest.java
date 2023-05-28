package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cart.exception.UsedCouponException;

public class CouponTest {

    @DisplayName("쿠폰을 사용해 할인한다")
    @Test
    void discount_using_coupon() {
        Coupon coupon = new Coupon(1L, 10000, DiscountPolicy.FIXED);

        assertThat(coupon.discount(new Money(12000))).isEqualTo(new Money(2000));
    }

    @DisplayName("쿠폰은 다시 사용할 수 없다")
    @Test
    void cannot_be_reused() {
        Coupon coupon = new Coupon(1L, 10000, DiscountPolicy.FIXED);

        coupon.discount(new Money(12000));

        assertThatThrownBy(() -> coupon.discount(new Money(13000)))
                .isInstanceOf(UsedCouponException.class);
    }
}
