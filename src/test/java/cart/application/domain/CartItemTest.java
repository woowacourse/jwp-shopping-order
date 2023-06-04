package cart.application.domain;

import cart.application.exception.IllegalMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    @DisplayName("상품 수량을 변경할 수 있다")
    void updateQuantity() {
        // given
        Product product = new Product(0L, "", 1000, "", 10.0, true);
        Member member = new Member(0L, "", "", 0L);
        CartItem cartItem = new CartItem(0L, 5, product, member);
        // when
        cartItem.updateQuantity(3L);
        // then
        assertThat(cartItem.getQuantity()).isEqualTo(3L);
    }

    @Test
    @DisplayName("장바구니 품목을 담은 멤버가 아니라면 예외를 던진다")
    void validateIsOwnedBy_exception() {
        // given
        Product product = new Product(0L, "", 1000, "", 10.0, true);
        Member member = new Member(0L, "", "", 0L);
        Member another = new Member(1L, "", "", 0L);
        CartItem cartItem = new CartItem(0L, 5, product, member);
        // when, then
        assertThatThrownBy(() -> cartItem.validateIsOwnedBy(another))
                .isInstanceOf(IllegalMemberException.class);
    }

    @Test
    @DisplayName("장바구니 품목을 담은 멤버라면 예외를 던지지 않는다")
    void validateIsOwnedBy() {
        // given
        Product product = new Product(0L, "", 1000, "", 10.0, true);
        Member member = new Member(0L, "", "", 0L);
        CartItem cartItem = new CartItem(0L, 5, product, member);
        // when, then
        assertThatCode(() -> cartItem.validateIsOwnedBy(member))
                .doesNotThrowAnyException();
    }
}
