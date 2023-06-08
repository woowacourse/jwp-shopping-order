package cart.domain.coupon;

import cart.exception.CouponException;
import cart.exception.NoExpectedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CouponTest {
    private AmountCoupon amountCoupon;
    private PercentageCoupon percentageCoupon;

    @BeforeEach
    void setup() {
        CouponInfo amountCouponInfo = new CouponInfo(1L, "금액", 10000, 1000);
        amountCoupon = new AmountCoupon(amountCouponInfo, 1000);
        CouponInfo percentageCouponInfo = new CouponInfo(1L, "퍼센트", 10000, 5000);
        percentageCoupon = new PercentageCoupon(percentageCouponInfo, 0.3);
    }
    @Test
    public void 쿠폰이_사용가능한지_판단한다() {
        boolean isAvailable = amountCoupon.isAvailable(12000);

        assertThat(isAvailable).isTrue();
    }

    @Test
    public void 쿠폰_최소_사용금액_보다_높지_않으면_false를_반환한다() {
        boolean isAvailable = amountCoupon.isAvailable(8000);

        assertThat(isAvailable).isFalse();
    }

    @Test
    public void 금액_쿠폰의_할인액을_계산한다() {
        int discount = amountCoupon.calculateDiscount(12000);

        assertThat(discount).isEqualTo(1000);
    }

    @Test
    public void 최소_주문_금액_보다_작으면_예외를_발생한다() {
        assertThatThrownBy(() -> amountCoupon.calculateDiscount(8000))
                .isInstanceOf(CouponException.Unavailable.class);
    }

    @Test
    public void 퍼센트_쿠폰의_할인_할인액을_계산한다() {
        int discount = percentageCoupon.calculateDiscount(10000);

        assertThat(discount).isEqualTo(3000);
    }

    @Test
    public void 퍼센트_쿠폰의_할인액이_최대_할인금액을_넘으면_최대_할인금액만큼_할인된다() {
        int discount = percentageCoupon.calculateDiscount(1000000);

        assertThat(discount).isEqualTo(5000);
    }

    @Test
    public void 퍼센트_쿠폰의_최소_주문_끔액_보다_작으면_예외를_발생한다() {
        assertThatThrownBy(() -> percentageCoupon.calculateDiscount(8000))
                .isInstanceOf(CouponException.Unavailable.class);
    }

}
