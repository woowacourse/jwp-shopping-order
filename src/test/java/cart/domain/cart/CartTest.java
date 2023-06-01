package cart.domain.cart;

import cart.domain.coupon.Coupons;
import cart.domain.product.Product;
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

    @DisplayName("카트에 아이템을 추가한다.")
    @Test
    void add_item_success() {
        // given
        Cart cart = createCart();

        // when
        cart.addItem(new Product(3L, "족발", 10000, "", false, 0));

        // then
        assertThat(cart.getCartItems().size()).isEqualTo(3);
    }

    @DisplayName("카트에 아이템을 제거한다.")
    @Test
    void delete_item_success() {
        // given
        Cart cart = createCart();
        CartItem cartItem = cart.getCartItems().get(0);

        // when
        cart.removeItem(cartItem);

        // then
        assertThat(cart.getCartItems().size()).isEqualTo(1);
    }

    @DisplayName("장바구니에 상품 수량을 변경한다.")
    @Test
    void update_quantity() {
        // given
        Cart cart = createCart();

        // when
        cart.changeQuantity(1, 20);

        // then
        assertThat(cart.getCartItems().get(0).getQuantity()).isEqualTo(20);
    }

    @DisplayName("장바구니에 아이템이 존재하는지 확인한다.")
    @Test
    void check_is_exist_item() {
        // given
        Cart cart = createCart();
        CartItem cartItem = cart.getCartItems().get(0);

        // when
        boolean result = cart.hasCartItem(cartItem);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("장바구니에 담긴 상품의 가격을 조회한다. (할인 전 가격)")
    @Test
    void returns_origin_prices_of_products() {
        // given
        Cart cart = createCart();

        // when
        int price = cart.calculateOriginPrice();

        // then
        assertThat(price).isEqualTo(30000);
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
