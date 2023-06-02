package cart.domain;

import cart.domain.member.Member;
import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static cart.common.fixture.DomainFixture.MEMBER_HUCHU;
import static cart.common.fixture.DomainFixture.PRODUCT_CHICKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CartItemTest {


    @Test
    void 주인을_확인할_때_같은_회원이면_예외를_던지지_않는다() {
        //given
        final CartItem cartItem = new CartItem(1L, 1, PRODUCT_CHICKEN, MEMBER_HUCHU);

        //when
        assertThatNoException().isThrownBy(() -> cartItem.checkOwner(MEMBER_HUCHU));
    }

    @Test
    void 주인을_확인할_때_다른_회원이면_예외를_던진다() {
        //given
        final CartItem cartItem = new CartItem(1L, 1, PRODUCT_CHICKEN, MEMBER_HUCHU);

        //expect
        assertThatThrownBy(() -> cartItem.checkOwner(new Member(2L, "hamad@woowahan.com", "1234567a!", 1000)))
                .isInstanceOf(CartItemException.class)
                .hasMessage("Illegal member attempts to cart; cartItemId=1, memberId=2");
    }

    @Test
    void 수량을_변경한다() {
        //given
        final CartItem cartItem = new CartItem(1L, 1, PRODUCT_CHICKEN, MEMBER_HUCHU);

        //when
        cartItem.changeQuantity(2);

        //then
        assertThat(cartItem.getQuantity()).isEqualTo(2);
    }

    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void 같은_상품인지_상품_id로_확인한다(final Long productId, final boolean expected) {
        //given
        final CartItem cartItem = new CartItem(1L, 1, PRODUCT_CHICKEN, MEMBER_HUCHU);

        //when
        final boolean actual = cartItem.hasSameProduct(productId);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
