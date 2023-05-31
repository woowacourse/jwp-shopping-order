package cart.domain;

import cart.exception.NegativeCouponException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponTest {

    @DisplayName("쿠폰은 금액은 양수이다.")
    @Test
    void constructor_invalid() {
        //given, when, then
        assertThatThrownBy(() -> new Coupon(1L, -1000, "돈 더 내는 쿠폰"))
                .isInstanceOf(NegativeCouponException.class);
    }

    @DisplayName("주문 금액을 입력하면 쿠폰 할인금 만큼 가격을 할인해서 반환한다.")
    @Test
    void discount() {
        //given
        final Coupon coupon = new Coupon(1L, 1000, "1000원 할인 쿠폰");

        //when
        final int discountedPrice = coupon.calculateDiscount();

        //then
        assertThat(discountedPrice).isEqualTo(1000);
    }
    
}
