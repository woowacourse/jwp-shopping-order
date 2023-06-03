package cart.domain;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.DiscountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponTest {

    @Test
    @DisplayName("고정 할인 쿠폰 가격을 적용할 수 있다.")
    void applyReductionCouponPrice() {
        Coupon coupon = new Coupon("5000원 할인 쿠폰", DiscountType.from("deduction"), 10000, 5000, 0);
        assertThat(coupon.applyCouponPrice(10000)).isEqualTo(5000);
    }

    @Test
    @DisplayName("정률 할인 쿠폰 가격을 적용할 수 있다.")
    void applyPercentCouponPrice() {
        Coupon coupon = new Coupon("50%할인 쿠폰", DiscountType.from("percentage"), 10000, 0, 0.5);
        assertThat(coupon.applyCouponPrice(10000)).isEqualTo(5000);
    }

    @Test
    @DisplayName("최소 금액 이상 시에만 사용 가능")
    void applyCouponPriceOverMinimumPrice() {
        Coupon coupon = new Coupon("5000원 할인 쿠폰", DiscountType.from("percentage"), 10000, 0, 0.5);
        assertThatThrownBy(() -> coupon.applyCouponPrice(9999)).hasMessage("사용할 수 없는 쿠폰 범위 입니다.");
    }
}
