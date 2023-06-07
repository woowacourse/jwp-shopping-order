package cart.domain.cart;

import cart.domain.coupon.Coupons;
import cart.dto.history.OrderedProductHistory;
import cart.dto.product.ProductPriceAppliedAllDiscountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixture.CartFixture.createCart;
import static cart.fixture.CouponFixture.createCoupons;
import static cart.fixture.CouponFixture.createDeliveryCoupon;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class CartTest {

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

    @DisplayName("요청한 상품을 구매한다.")
    @Test
    void buy_product() {
        // given
        List<Long> productIds = List.of(1L, 2L);
        List<Integer> quantities = List.of(1, 1);
        Cart cart = createCart();

        // when
        List<OrderedProductHistory> productHistories = cart.buy(productIds, quantities);

        // then
        assertAll(
                () -> assertThat(productHistories.get(0).getPrice()).isEqualTo(20000),
                () -> assertThat(productHistories.get(1).getPrice()).isEqualTo(9000)
        );
    }

    @DisplayName("상품 할인 및 쿠폰을 모두 적용한 금액을 반환한다.")
    @Test
    void returns_result_price_using_coupons_and_discounted() {
        // given
        Cart cart = createCart();
        Coupons coupons = createCoupons();

        // when
        List<ProductPriceAppliedAllDiscountResponse> result = cart.getProductUsingCouponAndSaleResponses(coupons.getCoupons());

        // then
        assertAll(
                () -> assertThat(result.get(0).getOriginalPrice()).isEqualTo(20000),
                () -> assertThat(result.get(0).getDiscountPrice()).isEqualTo(2900),
                () -> assertThat(result.get(1).getOriginalPrice()).isEqualTo(9000),
                () -> assertThat(result.get(1).getDiscountPrice()).isEqualTo(1800)
        );
    }
}
