package cart.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CartItemTest {

    @Test
    void 장바구니의_주인을_확인한다() {
        // given
        Member member = new Member("email@email.com", "password");
        Product product = new Product("지구", 10000, "http://image.com");
        CartItem cartItem = new CartItem(product, member);

        // expect
        assertDoesNotThrow(() -> cartItem.checkOwner(member));
    }

    @Test
    void 장바구니의_주인이_아닐_경우_예외가_발생한다() {
        // given
        Member member = new Member("email@email.com", "password");
        Product product = new Product("지구", 10000, "http://image.com");
        CartItem cartItem = new CartItem(product, member);

        // when
        Member other = new Member("other@email.com", "password");

        // then
        assertThatThrownBy(() -> cartItem.checkOwner(other))
                .isInstanceOf(CartItemException.IllegalMember.class);
    }

    @Test
    void 장바구니에_담긴_상품_수량을_변경한다() {
        // given
        Member member = new Member("email@email.com", "password");
        Product product = new Product("지구", 10000, "http://image.com");
        CartItem cartItem = new CartItem(product, member);

        // when
        cartItem.changeQuantity(100);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(100);
    }

    @Test
    void 장바구니에_담긴_상품들의_가격을_계산한다() {
        Member member = new Member("email@email.com", "password");
        Product product = new Product("지구", 10000, "http://image.com");
        CartItem cartItem = new CartItem(1L, 10, product, member);

        Money cartPrice = cartItem.calculateCartPrice();

        assertThat(cartPrice).isEqualTo(new Money(100000));
    }
}
