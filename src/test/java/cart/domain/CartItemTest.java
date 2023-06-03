package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.auth.AuthorizationException;
import cart.exception.order.CartItemQuantityDoesNotMatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CartItem 단위테스트")
class CartItemTest {

    @Test
    void cartitem은_product와_quantity와_member를_가진다() {
        // given
        Product chicken = new Product("치킨", 5000, "http://chickenimageutl", 30);
        Member member = new Member(1L, "a@a.com", "1234", new Point(3000));

        // when
        CartItem cartItem = new CartItem(1L, 3, chicken, member);

        // then
        assertThat(cartItem.getProduct()).isNotNull();
        assertThat(cartItem.getQuantity()).isNotNull();
        assertThat(cartItem.getMember()).isNotNull();
    }

    @Test
    void cartItem의_소유자를_체크할_수_있다() {
        // given
        Product chicken = new Product("치킨", 5000, "http://chickenimageutl", 30);
        Member member = new Member(1L, "a@a.com", "1234", new Point(3000));
        CartItem cartItem = new CartItem(1L, 3, chicken, member);

        // when
        Member illegalMember = new Member(2L, "b@b.com", "5678", new Point(3000));

        // then
        assertThatThrownBy(() -> cartItem.checkOwner(illegalMember))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("해당 member의 cartItem이 아닙니다.");
    }

    @Test
    void cartItem의_quantity를_체크할_수_있다() {
        // given
        Product chicken = new Product("치킨", 5000, "http://chickenimageutl", 30);
        Member member = new Member(1L, "a@a.com", "1234", new Point(3000));

        // when
        CartItem cartItem = new CartItem(1L, 3, chicken, member);

        // then
        assertThatThrownBy(() -> cartItem.checkQuantity(5))
                .isInstanceOf(CartItemQuantityDoesNotMatchException.class);
    }

    @Test
    void cartItem의_quantity를_업데이트_할_수_있다() {
        // given
        Product chicken = new Product("치킨", 5000, "http://chickenimageutl", 30);
        Member member = new Member(1L, "a@a.com", "1234", new Point(3000));

        // when
        CartItem cartItem = new CartItem(1L, 3, chicken, member);
        cartItem.changeQuantity(5);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(5);
    }
}
