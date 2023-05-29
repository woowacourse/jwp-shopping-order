package cart.domain.cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixture.CartFixture.createCart;
import static cart.fixture.CouponFixture.createDeliveryCoupon;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class CartTest {

    @DisplayName("쿠폰 적용 전 장바구니에 담긴 상품을 계산한다.")
    @Test
    void calculate_origin_price() {
        // given
        Cart cart = createCart();

        // when
        int result = cart.calculateOriginPrice();

        // then
        assertThat(result).isEqualTo(30000);
    }

    @DisplayName("쿠폰 적용 후 배달료를 계산한다.")
    @Test
    void calculate_after_apply_delivery_coupon_price() {
        // given
        Cart cart = createCart();

        // when
        int result = cart.calculateDeliveryFeeUsingCoupons(List.of(createDeliveryCoupon()));

        // then
        assertThat(result).isEqualTo(0);
    }
}
