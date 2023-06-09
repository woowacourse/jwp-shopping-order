package cart.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartItemTest {

    @DisplayName("장바구니에 담은 멤버와 같은 멤버가 주어지는 경우는 예외가 발생하지 않는다.")
    @Test
    void checkOwner() {
        // given
        Member 저문 = new Member(1L, "jeomxon@gmail.com", "abcd1234@");
        Product product = new Product("치킨", 10_000, "image.url", true, 20);
        CartItem cartItem = new CartItem(저문, product);

        // when, then
        assertDoesNotThrow(() -> cartItem.checkOwner(저문));
    }

    @DisplayName("장바구니에 담은 멤버와 다른 멤버가 주어지는 경우는 예외가 발생한다.")
    @Test
    void checkOwnerThrowsException() {
        // given
        Member 저문 = new Member(1L, "jeomxon@gmail.com", "abcd1234@");
        Product product = new Product("치킨", 10_000, "image.url", true, 20);
        CartItem cartItem = new CartItem(저문, product);

        Member 찰리 = new Member(2L, "charlie@gmail.com", "abcd1234@");

        // when, then
        assertThatThrownBy(() -> cartItem.checkOwner(찰리))
                .isInstanceOf(CartItemException.class);
    }
}
