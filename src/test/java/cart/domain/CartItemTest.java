package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.CartItemException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartItemTest {

    @DisplayName("장바구니 상품의 소유자가 해당 멤버가 맞을 경우 예외가 발생하지 않는다.")
    @Test
    void checkOwner() {
        //given
        final Member member = new Member(1L, "email", "password");
        final Product product = new Product("product", 1000, "imageUrl");
        final CartItem cartItem = new CartItem(member, product);

        //when
        //then
        Assertions.assertDoesNotThrow(() -> cartItem.checkOwner(member));
    }

    @DisplayName("장바구니 상품의 소유자가 해당 멤버가 아닐 경우 예외를 발생한다.")
    @Test
    void checkOwner_fail() {
        //given
        final Member member1 = new Member(1L, "email", "password");
        final Member member2 = new Member(2L, "email2", "password2");
        final Product product = new Product("product", 1000, "imageUrl");
        final CartItem cartItem = new CartItem(member1, product);

        //when
        //then
        assertThatThrownBy(() -> cartItem.checkOwner(member2)).
            isInstanceOf(CartItemException.IllegalMember.class);
    }

    @DisplayName("장바구니 상품의 수량을 변경한다.")
    @Test
    void changeQuantity() {
        //given
        final Member member = new Member(1L, "email", "password");
        final Product product = new Product("product", 1000, "imageUrl");
        final CartItem cartItem = new CartItem(member, product);

        //when
        final int prevQuantity = cartItem.getQuantity();
        cartItem.changeQuantity(20);

        //then
        assertThat(prevQuantity).isOne();
        assertThat(cartItem.getQuantity()).isEqualTo(20);
    }

    @DisplayName("장바구니 상품의 수량을 1보다 작은 값으로 변경시 예외를 발생한다.")
    @Test
    void changeQuantity_underOne() {
        //given
        final Member member = new Member(1L, "email", "password");
        final Product product = new Product("product", 1000, "imageUrl");
        final CartItem cartItem = new CartItem(member, product);

        //when
        //then
        assertThatThrownBy(() -> cartItem.changeQuantity(0))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
