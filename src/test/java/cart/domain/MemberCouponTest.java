package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.vo.Amount;
import cart.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberCouponTest {

    @Test
    @DisplayName("총 상품 금액에 쿠폰을 적용한다.")
    void testCalculateProduct() {
        // given
        final Coupon coupon = new Coupon(1L, "name", Amount.of(1_000), Amount.of(10_000));
        final MemberCoupon memberCoupon = new MemberCoupon(1L, 1L, coupon, false);
        final Amount productAmount = Amount.of(15_000);

        // when
        final Amount discountedAmount = memberCoupon.calculateProduct(productAmount);

        // then
        assertThat(discountedAmount).isEqualTo(Amount.of(14_000));
    }

    @Test
    @DisplayName("총 상품 금액이 최소 금액보다 작으면 적용할 수 없다.")
    void testCalculateProductWhenProductAmountIsLessThanMinAmount() {
        // given
        final Coupon coupon = new Coupon(1L, "name", Amount.of(1_000), Amount.of(10_000));
        final MemberCoupon memberCoupon = new MemberCoupon(1L, 1L, coupon, false);
        final Amount productAmount = Amount.of(9_000);

        // when & then
        assertThatThrownBy(() -> memberCoupon.calculateProduct(productAmount))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("이미 사용한 쿠폰이면 사용할 수 없다.")
    void testCalculateProductWhenUsed() {
        // given
        final Coupon coupon = new Coupon(1L, "name", Amount.of(1_000), Amount.of(10_000));
        final MemberCoupon memberCoupon = new MemberCoupon(1L, 1L, coupon, true);
        final Amount productAmount = Amount.of(9_000);

        // when & then
        assertThatThrownBy(() -> memberCoupon.calculateProduct(productAmount))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("쿠폰을 사용한 상태로 변경할 수 있다.")
    void testUse() {
        // given
        final Coupon coupon = new Coupon(1L, "name", Amount.of(1_000), Amount.of(10_000));
        final MemberCoupon memberCoupon = new MemberCoupon(1L, 1L, coupon, false);

        // when
        memberCoupon.use();

        // then
        assertThat(memberCoupon.isUsed()).isTrue();
    }
}
