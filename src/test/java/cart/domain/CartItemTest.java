package cart.domain;

import cart.exception.CartItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartItemTest {

    private Member member1;
    private Member member2;
    private Product product;

    @BeforeEach
    void setUp() {
        member1 = new Member(1L, "a@a.com", "1234", 100);
        member2 = new Member(2L, "b@b.com", "1234", 100);
        product = new Product(1L, "name", 1_000, "imageUrl");
    }

    @DisplayName("장바구니 상품 소유자와 멤버 id가 다를 경우 Exception을 반환한다.")
    @Test
    void checkOwnerTest() {
        final CartItem cartItem = new CartItem(member1, product);

        assertThrows(CartItemException.IllegalMember.class, () -> cartItem.checkOwner(member2));
    }

    @DisplayName("장바구니에 담긴 상품의 수량을 변경한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 10, 1_000})
    void changeQuantityTest(final int expectQuantity) {
        final CartItem cartItem = new CartItem(member1, product);

        cartItem.updateQuantity(expectQuantity);

        assertThat(cartItem.getQuantity()).isEqualTo(expectQuantity);
    }

    @DisplayName("변경 수량이 1보다 작을 경우 Exception을 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -1_000})
    void illegalChangeQuantityTest(final int expectQuantity) {
        final CartItem cartItem = new CartItem(member1, product);

        assertThrows(IllegalArgumentException.class, () -> cartItem.updateQuantity(expectQuantity));
    }
}
