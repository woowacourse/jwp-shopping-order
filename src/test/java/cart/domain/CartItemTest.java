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
                .hasMessageContaining("해당 회원의 장바구니가 아닙니다. memberId : " + null);
    }
}
