package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.dto.history.OrderedProductHistory;
import cart.dto.product.ProductPriceAppliedAllDiscountResponse;
import cart.exception.CartItemNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixture.CartItemsFixture.createCartItems;
import static cart.fixture.CouponFixture.createCoupons;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class CartItemsTest {

    @DisplayName("아이템을 구매한다.(상품 세일 적용된 가격을 가져온다. 세일이 안 됐다면 원래 가격을 가져온다.)")
    @Test
    void buy_items_result() {
        // given
        CartItems cartItems = createCartItems();
        Long productId = 1L;
        int quantity = 1;

        CartItem cartItem = cartItems.getCartItems().get(0);
        cartItem.getProduct().applySale(10);

        // when
        OrderedProductHistory result = cartItems.buy(productId, quantity);

        // then
        assertAll(
                () -> assertThat(result.getPrice()).isEqualTo(18000),
                () -> assertThat(result.getQuantity()).isEqualTo(1),
                () -> assertThat(cartItems.getCartItems().get(0).getQuantity()).isEqualTo(9)
        );
    }

    @DisplayName("구매시에 요청한 상품을 찾을 수 없다면 예외를 발생한다.")
    @Test
    void throws_exception_when_not_found_cart_item() {
        // given
        CartItems cartItems = createCartItems();

        // when & then
        assertThatThrownBy(() -> cartItems.buy(10L, 1))
                .isInstanceOf(CartItemNotFoundException.class);
    }

    @DisplayName("모든 쿠폰 포함 할인 정책이 적용된 상품을 계산한다.")
    @Test
    void returns_product_prices_applied_all_discount_policy() {
        // given
        List<Coupon> coupons = createCoupons().getCoupons();
        CartItems cartItems = createCartItems();

        // when
        List<ProductPriceAppliedAllDiscountResponse> result = cartItems.getProductPricesAppliedAllDiscount(coupons);
        for (ProductPriceAppliedAllDiscountResponse i : result) {
            System.out.println(i.getProductId() + " " + i.getOriginalPrice() + " " + i.getDiscountPrice());
        }

        // then
        assertAll(
                () -> assertThat(result.get(0).getOriginalPrice()).isEqualTo(20000),
                () -> assertThat(result.get(0).getDiscountPrice()).isEqualTo(2900),
                () -> assertThat(result.get(1).getOriginalPrice()).isEqualTo(9000),
                () -> assertThat(result.get(1).getDiscountPrice()).isEqualTo(1800)
        );
    }
}
