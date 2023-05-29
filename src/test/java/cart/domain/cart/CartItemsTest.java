package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.order.ProductHistory;
import cart.domain.product.Product;
import cart.dto.product.ProductPriceAppliedAllDiscountResponse;
import cart.exception.CartItemNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixture.CartItemFixture.createCartItem;
import static cart.fixture.CartItemsFixture.createCartItems;
import static cart.fixture.CouponFixture.createCoupons;
import static cart.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class CartItemsTest {

    @DisplayName("카트 아이템을 추가할 때 이미 있으면 수량을 증가시킨다.")
    @Test
    void add_quantity_when_has_cart_item_already() {
        // given
        CartItems cartItems = createCartItems();
        Product product = createProduct();

        // when
        cartItems.add(product);

        // then
        assertAll(
                () -> assertThat(cartItems.getCartItems().size()).isEqualTo(2),
                () -> assertThat(cartItems.getCartItems().get(0).getQuantity()).isEqualTo(11)
        );
    }

    @DisplayName("카트 아이템을 추가할 때 없는 상품이라면 추가시킨다.")
    @Test
    void add_item_when_has_not_cart_item() {
        // given
        CartItems cartItems = createCartItems();
        Product product = createProduct(3L, "보쌈");

        // when
        cartItems.add(product);

        // then
        assertThat(cartItems.getCartItems().size()).isEqualTo(3);
    }

    @DisplayName("카트 아이템을 삭제한다.")
    @Test
    void remove_cart_item() {
        // given
        CartItems cartItems = createCartItems();
        CartItem cartItem = createCartItem();

        // when
        cartItems.remove(cartItem);

        // then
        assertThat(cartItems.getCartItems().size()).isEqualTo(1);
    }

    @DisplayName("아이템의 수량을 변경 시킨다.")
    @Test
    void change_cart_item_quantity() {
        // given
        CartItems cartItems = createCartItems();

        // when
        cartItems.changeQuantity(1, 10);

        // then
        assertThat(cartItems.getCartItems().get(0).getQuantity()).isEqualTo(10);
    }

    @DisplayName("변경하려는 상품의 개수가 0이면 장바구니에서 삭제한다.")
    @Test
    void remove_cart_item_when_changed_quantity_is_zero() {
        // given
        CartItems cartItems = createCartItems();

        // when
        cartItems.changeQuantity(1, 0);

        // then
        assertThat(cartItems.getCartItems().size()).isEqualTo(1);
    }

    @DisplayName("아이템을 찾을 수 없다면 예외를 발생 시킨다.")
    @Test
    void throws_exception_when_not_fount_cart_item() {
        // given
        CartItems cartItems = createCartItems();

        // when  & then
        assertThatThrownBy(() -> cartItems.changeQuantity(10, 10))
                .isInstanceOf(CartItemNotFoundException.class);
    }

    @DisplayName("아이템을 가지고 있는지 확인한다.")
    @Test
    void check_has_cart_item() {
        // given
        CartItems cartItems = createCartItems();
        CartItem cartItem = createCartItem();

        // when
        boolean result = cartItems.hasCartItem(cartItem);

        // then
        assertThat(result).isTrue();
    }

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
        ProductHistory result = cartItems.buy(productId, quantity);

        // then
        assertAll(
                () -> assertThat(result.getPrice()).isEqualTo(18000),
                () -> assertThat(result.getQuantity()).isEqualTo(1),
                () -> assertThat(cartItems.getCartItems().get(0).getQuantity()).isEqualTo(9)
        );
    }

    @DisplayName("구매시에 요청한 상품을 찾을 수 없다면 예외를 발생한다.")
    @Test
    void thorws_exception_when_not_found_cart_item() {
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
