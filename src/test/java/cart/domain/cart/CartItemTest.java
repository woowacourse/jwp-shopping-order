package cart.domain.cart;

import static cart.fixture.ProductFixture.상품_8900원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.cart.InvalidCartItemOwnerException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CartItemTest {

    @Test
    void 수량의_초기값은_1이다() {
        // given
        final CartItem cartItem = new CartItem(1L, 상품_8900원);

        // expect
        assertThat(cartItem.getQuantity()).isEqualTo(1);
    }

    @Test
    void 수량을_변경한다() {
        // given
        final CartItem cartItem = new CartItem(1L, 상품_8900원);

        // when
        cartItem.changeQuantity(2);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(2);
    }

    @Test
    void 소유주가_아니라면_예외를_던진다() {
        // given
        final CartItem cartItem = new CartItem(1L, 상품_8900원);

        // expect
        assertThatThrownBy(() -> cartItem.checkOwner(Long.MAX_VALUE))
                .isInstanceOf(InvalidCartItemOwnerException.class)
                .hasMessage("장바구니의 소유자가 아닙니다.");
    }

    @Test
    void 소유주가_맞다면_예외를_던지지_않는다() {
        // given
        final CartItem cartItem = new CartItem(1L, 상품_8900원);

        // expect
        assertThatNoException().isThrownBy(() -> cartItem.checkOwner(1L));
    }
}
