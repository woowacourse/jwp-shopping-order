package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("장바구니 도메인 테스트")
class CartItemTest {

    @DisplayName("장바구니의 담은 수량은 자연수여야 한다.")
    @ValueSource(ints = {1, 5, 10, 100, 10000, 1000000})
    @ParameterizedTest
    void quantityOverThanZero(int quantity) {
        //given
        Member member = null;
        Product product = new Product(null, 0, null, 100000000);

        //when
        CartItem cartItem = new CartItem(null, quantity, product, member);

        //then
        assertEquals(quantity, cartItem.getQuantity());
    }

    @DisplayName("장바구니의 담은 수량이 0이하인 경우 예외를 던진다.")
    @ValueSource(ints = {0, -1, -10, -100, -1000, -10000})
    @ParameterizedTest
    void quantityUnderThanZeroThrowException(int quantity) {
        //given
        Member member = null;
        Product product = new Product(null, 0, null, 100000);

        //when, then
        assertThatThrownBy(() -> new CartItem(null, quantity, product, member))
                .isInstanceOf(IllegalArgumentException.class)
                .describedAs("수량은 자연수여야 합니다.");
    }
}
