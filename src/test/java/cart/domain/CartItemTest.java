package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.IllegalAccessCartException;
import org.junit.jupiter.api.Test;

class CartItemTest {

    @Test
    void 특정_회원의_소유인지_검증한다_같은_회원이면_예외가_발생하지_않는다() {
        // given
        Member member = new Member(1L, "email", "pw", 10);
        CartItem cartItem = new CartItem(1L, new Product("productName", 10_000, "productImageUrl", 100), member, 1);

        // expect
        assertThatNoException().isThrownBy(() -> cartItem.validateOwner(member));
    }

    @Test
    void 특정_회원의_소유인지_검증한다_다른_회원이면_예외가_발생한다() {
        // given
        Member member = new Member(1L, "email", "pw", 10);
        CartItem cartItem = new CartItem(1L, new Product("productName", 10_000, "productImageUrl", 100), member, 1);

        Member otherMember = new Member(2L, "otherEmail", "otherPw", 0);

        // expect
        assertThatThrownBy(() -> cartItem.validateOwner(otherMember))
                .isInstanceOf(IllegalAccessCartException.class);
    }

    @Test
    void 수량을_변경한다() {
        // given
        int quantity = 1;
        CartItem cartItem = new CartItem(1L, new Product("a", 10_000, "b", 10), new Member(1L, "email", "pw", 10), quantity);

        // when
        int newQuantity = 2;
        CartItem newCartItem = cartItem.changeQuantity(newQuantity);

        // then
        assertThat(newCartItem.getQuantity()).isEqualTo(newQuantity);
    }

    @Test
    void 모든_필드가_같으면_같은_객체다() {
        // given
        CartItem cartItem = new CartItem(1L, new Product("a", 10_000, "b", 10), new Member(1L, "email", "pw", 10), 1);
        CartItem sameCartItem = new CartItem(1L, new Product("a", 10_000, "b", 10), new Member(1L, "email", "pw", 10), 1);

        // expect
        assertThat(cartItem).isEqualTo(sameCartItem);
    }
}
