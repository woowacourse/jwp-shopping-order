package cart.domain;

import cart.exception.CartItemException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartItemTest {

    private final long validId = 1L;
    private final int validQuantity = 2;
    private final Product validProduct = new Product("상품 이름", 1_000, "https://i.guim.co.uk/img/media/26392d05302e02f7bf4eb143bb84c8097d09144b/446_167_3683_2210/master/3683.jpg?width=1200&quality=85&auto=format&fit=max&s=a52bbe202f57ac0f5ff7f47166906403");
    private final Member validMember = new Member(1L, "a@a.com", "1234a!");

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void ZERO_이하의_수를_입력하면_예외를_던진다(int invalidQuantity) {
        assertThatThrownBy(() -> new CartItem(validId, invalidQuantity, validProduct, validMember))
                .isInstanceOf(CartItemException.InvalidQuantity.class)
                .hasMessageContaining("장바구니에 담긴 상품의 개수는 최소 1 이상이어야 합니다.");
    }

    @Test
    void 아이디를_입력하지_않으면_예외를_던진다() {
        assertThatThrownBy(() -> new CartItem(null, validQuantity, validProduct, validMember))
                .isInstanceOf(CartItemException.InvalidIdByNull.class)
                .hasMessageContaining("장바구니 아이디를 입력해야 합니다.");
    }

    @Test
    void 상품이_없으면_예외를_던진다() {
        assertThatThrownBy(() -> new CartItem(validId, validQuantity, null, validMember))
                .isInstanceOf(CartItemException.InvalidProduct.class)
                .hasMessageContaining("장바구니에 담으려는 상품이 존재하지 않습니다.");
    }

    @Test
    void 멤버가_없으면_예외를_던진다() {
        assertThatThrownBy(() -> new CartItem(validId, validQuantity, validProduct, null))
                .isInstanceOf(CartItemException.InvalidMember.class)
                .hasMessageContaining("장바구니에 접근하려는 멤버가 없습니다.");
    }
}
