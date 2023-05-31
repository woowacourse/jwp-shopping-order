package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.vo.Amount;
import cart.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AmountCouponTest {

    @Test
    @DisplayName("총 상품 금액에 쿠폰을 적용한다.")
    void testCalculateProduct() {
        //given
        final Coupon amountCoupon = new AmountCoupon(1L, "name", Amount.of(1_000), Amount.of(10_000), isUsed);
        final Amount productAmount = Amount.of(15_000);

        //when
        final Amount discountedAmount = amountCoupon.calculateProduct(productAmount);

        //then
        assertThat(discountedAmount).isEqualTo(Amount.of(14_000));
    }

    @Test
    @DisplayName("총 상품 금액이 최소 금액보다 작으면 적용할 수 없다.")
    void testCalculateProductWhenProductAmountIsLessThanMinAmount() {
        //given
        final Coupon amountCoupon = new AmountCoupon(1L, "name", Amount.of(1_000), Amount.of(10_000), isUsed);
        final Amount productAmount = Amount.of(9_000);

        //when
        //then
        assertThatThrownBy(() -> amountCoupon.calculateProduct(productAmount))
            .isInstanceOf(BusinessException.class);
    }
}
