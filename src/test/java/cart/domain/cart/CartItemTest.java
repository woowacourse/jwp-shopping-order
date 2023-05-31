package cart.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.VO.Money;
import cart.domain.member.Member;
import cart.exception.InvalidCartItemOwnerException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CartItemTest {

    @Test
    void 수량의_초기값은_1이다() {
        // given
        final Member member = new Member("pizza@pizza.com", "password");
        final Product product = new Product("pizza1", "pizza1.jpg", 8900L);
        final CartItem cartItem = new CartItem(member, product);

        // expect
        assertThat(cartItem.getQuantity()).isEqualTo(1);
    }

    @Test
    void 가격의_총합을_계산하여_반환한다() {
        // given
        final Member member = new Member("pizza@pizza.com", "password");
        final Product product = new Product("pizza1", "pizza1.jpg", 8900L);
        final CartItem cartItem = new CartItem(null, 3, member, product);

        // expect
        assertThat(cartItem.calculateTotalPrice()).isEqualTo(Money.from(26700));
    }

    @Test
    void 수량을_변경한다() {
        // given
        final Member member = new Member("pizza@pizza.com", "password");
        final Product product = new Product("pizza1", "pizza1.jpg", 8900L);
        final CartItem cartItem = new CartItem(member, product);

        // when
        cartItem.changeQuantity(2);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(2);
    }

    @Test
    void 소유주가_아니라면_예외를_던진다() {
        // given
        final Member member = new Member(1L, "pizza@pizza.com", "password");
        final Product product = new Product("pizza1", "pizza1.jpg", 8900L);
        final CartItem cartItem = new CartItem(member, product);

        // expect
        assertThatThrownBy(() -> cartItem.checkOwner(new Member(2L, "email", "password")))
                .isInstanceOf(InvalidCartItemOwnerException.class)
                .hasMessage("장바구니의 소유자가 아닙니다.");
    }

    @Test
    void 소유주가_맞다면_예외를_던지지_않는다() {
        // given
        final Member member = new Member(1L, "pizza@pizza.com", "password");
        final Product product = new Product("pizza1", "pizza1.jpg", 8900L);
        final CartItem cartItem = new CartItem(member, product);

        // expect
        assertThatNoException().isThrownBy(() -> cartItem.checkOwner(member));
    }
}
