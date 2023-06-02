package cart.domain.cartItem;

import cart.exception.CartItemException;
import cart.fixture.Fixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartItemTest {

    @Test
    void checkOwner() {
        CartItem cartItem = new CartItem(1L, 10, Fixture.치킨, Fixture.유저);

        assertThatThrownBy(() -> cartItem.checkOwner(Fixture.유저2)).isInstanceOf(CartItemException.IllegalMember.class);
    }

    @Test
    void changeQuantity() {
        CartItem cartItem = new CartItem(1L, 10, Fixture.치킨, Fixture.유저);

        CartItem updateCartItem = cartItem.changeQuantity(20);

        Assertions.assertThat(updateCartItem.getQuantity()).isEqualTo(20);
    }
}
