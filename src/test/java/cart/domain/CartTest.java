package cart.domain;

import cart.domain.Member.Member;
import cart.domain.Product.Product;
import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CartTest {

    @Test
    @DisplayName("장바구니에 담긴 상품이 모두 현재 사용자의 상품이 아니라면 예외가 발생한다.")
    void validateOwner_fail() {
        // given
        Member owner = new Member(1L, "a@a.com", "1234");
        Member other = new Member(2L, "b@b.com", "1234");
        Product product1 = Product.from("상품1", 10000, "sampleImg");
        Product product2 = Product.from("상품2", 10000, "sampleImg");

        Cart cart = new Cart(List.of(
                new CartItem(1L, 1, product1, owner),
                new CartItem(2L, 1, product2, other)
        ));

        // when, then
        assertThatThrownBy(() -> cart.checkOwner(owner))
                .isInstanceOf(CartItemException.class);
    }
}