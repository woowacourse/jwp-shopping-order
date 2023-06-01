package cart.domain.cartitem;

import static cart.fixtures.CartItemFixtures.Dooly_CartItem1;
import static cart.fixtures.CartItemFixtures.Dooly_CartItem2;
import static cart.fixtures.OrderFixtures.Dooly_Order1;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static cart.fixtures.ProductFixtures.PANCAKE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import cart.domain.product.Product;
import cart.dto.OrderCartItemDto;
import cart.exception.CartItemNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CartItemsTest {

    @Test
    @DisplayName("해당 상품이 장바구니 상품들에 포함되어 있으면 TRUE를 반환한다.")
    void isContainProduct_true() {
        // given
        CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));

        // when, then
        assertThat(cartItems.isContainProduct(CHICKEN.ENTITY())).isTrue();
    }

    @Test
    @DisplayName("해당 상품이 장바구니 상품들에 포함되어 있지 않으면 FALSE를 반환한다.")
    void isContainProduct_false() {
        // given
        CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));

        // when, then
        assertThat(cartItems.isContainProduct(PANCAKE.ENTITY())).isFalse();
    }

    @Test
    @DisplayName("상품에 해당하는 장바구니 상품을 반환한다.")
    void findCartItemByProduct() {
        // given
        CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));
        Product product = CHICKEN.ENTITY();

        // when
        CartItem cartItem = cartItems.findCartItemByProduct(product);

        // then
        assertThat(cartItem).usingRecursiveComparison().isEqualTo(Dooly_CartItem1.ENTITY());
    }

    @Test
    @DisplayName("상품에 해당하는 장바구니 상품 반환 시 해당하는 장바구니 상품이 없으면 예외가 발생한다.")
    void findCartItemByProduct_throw_not_exist() {
        // given
        CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));
        Product product = PANCAKE.ENTITY();

        // when, then
        assertThatThrownBy(() -> cartItems.findCartItemByProduct(product))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessage("상품에 해당하는 장바구니 상품을 찾을 수 없습니다.");
    }

    @Nested
    @DisplayName("OrderCartItemDto를 받아서 해당하는 장바구니 상품 목록 반환 시")
    class getCartItemsByOrderCartItemDtos {

        @Test
        @DisplayName("장바구니 상품과 주문할 장바구니 상품 이름이 다르면 예외가 발생한다.")
        void throws_not_match_cartItem_name() {
            // given
            List<OrderCartItemDto> orderCartItemDtos = Dooly_Order1.UPDATE_NAME_REQUEST().getOrderCartItemDtos();
            CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));

            // when
            assertThatThrownBy(() -> cartItems.getCartItemsByOrderCartItemDtos(orderCartItemDtos))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.");
        }

        @Test
        @DisplayName("장바구니 상품과 주문할 장바구니 상품 가격이 다르면 예외가 발생한다.")
        void throws_not_match_cartItem_price() {
            // given
            List<OrderCartItemDto> orderCartItemDtos = Dooly_Order1.UPDATE_PRICE_REQUEST().getOrderCartItemDtos();
            CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));

            // when
            assertThatThrownBy(() -> cartItems.getCartItemsByOrderCartItemDtos(orderCartItemDtos))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.");
        }

        @Test
        @DisplayName("장바구니 상품과 주문할 장바구니 상품 이미지가 다르면 예외가 발생한다.")
        void throws_not_match_cartItem_imageUrl() {
            // given
            List<OrderCartItemDto> orderCartItemDtos = Dooly_Order1.UPDATE_IMAGE_URL_REQUEST().getOrderCartItemDtos();
            CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));

            // when
            assertThatThrownBy(() -> cartItems.getCartItemsByOrderCartItemDtos(orderCartItemDtos))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.");
        }

        @Test
        @DisplayName("주문할 장바구니 상품 정보와 같은 장바구니 상품 목록을 반환한다.")
        void getMatchedCartItems() {
            // given
            Product product1 = Dooly_CartItem1.PRODUCT;
            Product product2 = Dooly_CartItem2.PRODUCT;
            List<OrderCartItemDto> orderCartItemDtos = List.of(
                    new OrderCartItemDto(Dooly_CartItem1.ID, product1.getName(), product1.getPrice(), product1.getImageUrl()),
                    new OrderCartItemDto(Dooly_CartItem2.ID, product2.getName(), product2.getPrice(), product2.getImageUrl())
            );
            CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));

            // when
            List<CartItem> cartItemsToOrder = cartItems.getCartItemsByOrderCartItemDtos(orderCartItemDtos);

            // then
            assertThat(cartItemsToOrder).usingRecursiveFieldByFieldElementComparator()
                    .contains(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY());
        }
    }

    @Test
    @DisplayName("장바구니 상품 목록의 총 가격을 구한다.")
    void getTotalPrice() {
        // given
        CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));
        int expectedTotalPrice = Dooly_CartItem1.PRICE + Dooly_CartItem2.PRICE;

        // when, then
        assertThat(cartItems.getTotalPrice()).isEqualTo(expectedTotalPrice);
    }
}
