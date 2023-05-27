package cart.domain.cart;

import cart.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cart.fixture.CartItemFixture.createCartItem;
import static cart.fixture.CartItemsFixture.createCartItems;
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
                () -> assertThat(cartItems.getCartItems().get(0).getQuantity()).isEqualTo(2)
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
                .isInstanceOf(IllegalArgumentException.class);
    }
}
