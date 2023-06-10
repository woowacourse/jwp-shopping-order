package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.vo.Amount;
import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartItemTest {

    @Test
    @DisplayName("장바구니 사용자가 아닌 경우, 예외를 발생한다.")
    void testCheckOwner() {
        // given
        final Member member = new Member(1L, "mango@wooteco.com", "password");
        final CartItem cartItem = new CartItem(1, new Product("망고", Amount.of(1_000), "mango.png"), member);
        final Member differentMember = new Member(2L, "turtle@wooteco.com", "password");

        // when & then
        assertThatThrownBy(() -> cartItem.checkOwner(differentMember))
                .isInstanceOf(CartItemException.IllegalMember.class);
    }

    @Test
    @DisplayName("장바구니의 상품 수량을 변경할 수 있다.")
    void testChangeQuantity() {
        // given
        final Member member = new Member(1L, "mango@wooteco.com", "password");
        final CartItem cartItem = new CartItem(1, new Product("망고", Amount.of(1_000), "mango.png"), member);
        final int newQuantity = 5;

        // when
        cartItem.changeQuantity(newQuantity);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(newQuantity);
    }

    @Test
    @DisplayName("장바구니의 상품에 수량을 더할 수 있다.")
    void testAddQuantity() {
        // given
        final Member member = new Member(1L, "mango@wooteco.com", "password");
        final CartItem cartItem = new CartItem(1, new Product("망고", Amount.of(1_000), "mango.png"), member);
        final int additionalQuantity = 3;

        // when
        final CartItem newCartItem = cartItem.addQuantity(additionalQuantity);

        // then
        assertThat(newCartItem.getQuantity()).isEqualTo(cartItem.getQuantity() + additionalQuantity);
    }
}
