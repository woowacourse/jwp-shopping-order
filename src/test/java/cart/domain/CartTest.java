package cart.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.CartItemException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartTest {

    @Test
    @DisplayName("장바구니에 담긴 상품이 모두 현재 사용자의 상품이 아니라면 예외가 발생한다.")
    void validateOwner_fail() {
        // given
        Member owner = new Member(1L, "a@a.com", "1234");
        Member other = new Member(2L, "b@b.com", "1234");
        Cart cart = new Cart(List.of(
            new CartItem(owner, new Product("치킨", Money.from(10000), "chickenImg")),
            new CartItem(other, new Product("치킨", Money.from(10000), "chickenImg"))
        ));

        // when, then
        assertThatThrownBy(() -> cart.validateOwner(owner))
            .isInstanceOf(CartItemException.IllegalMember.class);
    }
}