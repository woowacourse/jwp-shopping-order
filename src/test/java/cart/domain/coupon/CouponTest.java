package cart.domain.coupon;

import cart.domain.discountpolicy.AmountCoupon;
import cart.domain.discountpolicy.PercentCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CouponTest {

    @ParameterizedTest
    @CsvSource(value = {"0,0", "15,15000"})
    @DisplayName("쿠폰 생성 시 정량, 정액 할인 중 하나만 0이 아니면 예외 발생.")
    void validateExceptionTest(int percent, int amount) {
        assertThatThrownBy(() -> new Coupon("쿠폰", percent, amount, 4000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("쿠폰은 정량, 정액 할인 하나만 가능합니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"15,0", "0,14000"})
    @DisplayName("쿠폰 생성 시 정량, 정액 할인 중 하나만 0이면 정상수행")
    void validateTest(int percent, int amount) {
        assertDoesNotThrow(() -> new Coupon("쿠폰", percent, amount, 20000));
    }

    @Test
    @DisplayName("정률 할인이 0일 경우 정액 할인 인스턴스를 반환한다")
    void makeAmountCouponTest() {
        Coupon amoutCoupon = new Coupon("정액 할인 쿠폰", 0, 10000, 150000);
        assertThat(amoutCoupon.makeFitCouponType()).isInstanceOf(AmountCoupon.class);
    }

    @Test
    @DisplayName("정액 할인이 0일 경우 정률 할인 인스턴스를 반환한다")
    void makePercentCouponTest() {
        Coupon amoutCoupon = new Coupon("정액 할인 쿠폰", 10, 0, 150000);
        assertThat(amoutCoupon.makeFitCouponType()).isInstanceOf(PercentCoupon.class);
    }
}
