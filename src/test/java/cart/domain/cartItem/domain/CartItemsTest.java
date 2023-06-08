package cart.domain.cartItem.domain;

import static cart.fixtures.CartItemFixtures.Dooly_CartItem1;
import static cart.fixtures.CartItemFixtures.Dooly_CartItem2;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static cart.fixtures.ProductFixtures.PANCAKE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import cart.domain.cartitem.domain.CartItem;
import cart.domain.cartitem.domain.CartItems;
import cart.domain.product.domain.Product;
import cart.global.exception.CartItemNotFoundException;
import org.junit.jupiter.api.DisplayName;
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

    @Test
    @DisplayName("장바구니 상품 아이디 목록을 받아서 해당하는 장바구니 상품이 담긴 CartItems를 반환한다.")
    void getCartItemsByCartItemIds() {
        // given
        List<Long> cartItemIds = List.of(Dooly_CartItem1.ID, Dooly_CartItem2.ID);
        CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));

        // when
        CartItems findCartItems = cartItems.getCartItemsByCartItemIds(cartItemIds);

        // then
        assertThat(findCartItems.getCartItems()).usingRecursiveFieldByFieldElementComparator()
                .contains(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY());
    }

    @Test
    @DisplayName("장바구니 상품 ID로 장바구니 상품을 가져온다.")
    void getCartItemById_success() {
        // given
        Long cartItemId = Dooly_CartItem1.ID;
        CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));

        // when
        CartItem findCartItem = cartItems.getCartItemById(cartItemId);

        // then
        assertThat(findCartItem).usingRecursiveComparison().isEqualTo(Dooly_CartItem1.ENTITY());
    }

    @Test
    @DisplayName("장바구니 상품 ID로 장바구니 상품을 가져올 때 장바구니 상품이 없으면 예외가 발생한다.")
    void getCartItemById_throws_when_not_found_cartItem() {
        // given
        Long notExistId = -1L;
        CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));

        // when, then
        assertThatThrownBy(() -> cartItems.getCartItemById(notExistId))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessage("장바구니 상품에 없는 상품입니다.");
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
