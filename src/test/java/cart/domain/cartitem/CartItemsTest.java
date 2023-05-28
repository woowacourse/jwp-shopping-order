package cart.domain.cartitem;

import static cart.fixtures.CartItemFixtures.Dooly_CartItem1;
import static cart.fixtures.CartItemFixtures.Dooly_CartItem2;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static cart.fixtures.ProductFixtures.PANCAKE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import cart.domain.product.Product;
import cart.exception.CartItemNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartItemsTest {

    @Test
    @DisplayName("해당 상품이 장바구니 상품들에 포함되어 있으면 TRUE를 반환한다.")
    void isContainProduct_true() {
        // given
        CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY, Dooly_CartItem2.ENTITY));

        // when, then
        assertThat(cartItems.isContainProduct(CHICKEN.ENTITY)).isTrue();
    }

    @Test
    @DisplayName("해당 상품이 장바구니 상품들에 포함되어 있지 않으면 FALSE를 반환한다.")
    void isContainProduct_false() {
        // given
        CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY, Dooly_CartItem2.ENTITY));

        // when, then
        assertThat(cartItems.isContainProduct(PANCAKE.ENTITY)).isFalse();
    }

    @Test
    @DisplayName("상품에 해당하는 장바구니 상품을 반환한다.")
    void findCartItemByProduct() {
        // given
        CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY, Dooly_CartItem2.ENTITY));
        Product product = CHICKEN.ENTITY;

        // when
        CartItem cartItem = cartItems.findCartItemByProduct(product);

        // then
        assertThat(cartItem).usingRecursiveComparison().isEqualTo(Dooly_CartItem1.ENTITY);
    }

    @Test
    @DisplayName("상품에 해당하는 장바구니 상품 반환 시 해당하는 장바구니 상품이 없으면 예외가 발생한다.")
    void findCartItemByProduct_throw_not_exist() {
        // given
        CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY, Dooly_CartItem2.ENTITY));
        Product product = PANCAKE.ENTITY;

        // when, then
        assertThatThrownBy(() -> cartItems.findCartItemByProduct(product))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessage("상품에 해당하는 장바구니 상품을 찾을 수 없습니다.");
    }
}
