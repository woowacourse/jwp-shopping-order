package cart.domain.cartitem;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CartItemTest {

    @Nested
    @DisplayName("장바구니 상품 생성 시 ")
    class CreateCartItem {

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, 0})
        @DisplayName("개수가 1개보다 작으면 예외를 던진다.")
        void quantityShortage(int quantity) {
            Product product = new Product("치킨", 10000, "http://chicken.com");
            Member member = new Member("a@a.com", "password1", 10);

            assertThatThrownBy(() -> new CartItem(1L, quantity, product, member))
                    .isInstanceOf(CartItemException.class)
                    .hasMessage("장바구니 상품 수량은 최소 1개부터 가능합니다. 현재 개수: " + quantity);
        }
    }
}
