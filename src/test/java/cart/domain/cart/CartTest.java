package cart.domain.cart;

import cart.domain.coupon.Coupons;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixture.CartFixture.createCart;
import static cart.fixture.CouponFixture.createCoupons;
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

    @DisplayName("쿠폰 적용 후 장바구니에 담긴 상품을 계산한다.")
    @Test
    void calculate_after_apply_coupons_price() {
        // given
        Cart cart = createCart();
        cart.getMember().initCoupons(createCoupons());

        // when
        int result = cart.calculateItems(createCoupons().getCoupons());

        // then
        assertThat(result).isEqualTo(26100);
    }

    @DisplayName("쿠폰 적용 후 배달료를 계산한다.")
    @Test
    void calculate_after_apply_delivery_coupon_price() {
        // given
        Cart cart = createCart();
        cart.getMember().initCoupons(new Coupons(List.of(createDeliveryCoupon())));

        // when
        int result = cart.calculateDeliveryFee(List.of(createDeliveryCoupon()));

        // then
        assertThat(result).isEqualTo(0);
    }
}
