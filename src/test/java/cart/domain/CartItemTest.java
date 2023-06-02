package cart.domain;

import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static cart.fixture.MemberFixture.주노;
import static cart.fixture.ProductFixture.치킨;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CartItemTest {

    private final long validId = 1L;
    private final int validQuantity = 2;
    private final Product validProduct = new Product("상품 이름", 1_000, "https://i.guim.co.uk/img/media/26392d05302e02f7bf4eb143bb84c8097d09144b/446_167_3683_2210/master/3683.jpg?width=1200&quality=85&auto=format&fit=max&s=a52bbe202f57ac0f5ff7f47166906403");
    private final Member validMember = new Member(1L, "a@a.com", "1234a!");

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 장바구니의_상품_개수가_0_이하면_예외(int quantity) {
        assertThatThrownBy(() -> new CartItem(1L, quantity, 치킨.PRODUCT, 주노.MEMBER))
                .isInstanceOf(CartItemException.InvalidQuantity.class)
                .hasMessageContaining("장바구니에 담긴 상품의 개수는 최소 1 이상이어야 합니다.");
    }

    @Test
    void 상품이_없으면_예외() {
        assertThatThrownBy(() -> new CartItem(1L, 1, null, 주노.MEMBER))
                .isInstanceOf(CartItemException.InvalidProduct.class)
                .hasMessageContaining("장바구니에 담으려는 상품이 존재하지 않습니다.");
    }

    @Test
    void 회원이_없으면_예외() {
        assertThatThrownBy(() -> new CartItem(1L, 1, 치킨.PRODUCT, null))
                .isInstanceOf(CartItemException.InvalidMember.class)
                .hasMessageContaining("장바구니에 해당하는 회원이 존재하지 않습니다.");
    }
}
