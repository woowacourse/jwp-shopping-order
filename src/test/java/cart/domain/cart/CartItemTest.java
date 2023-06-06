package cart.domain.cart;

import cart.TestFixture;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CartItemTest {

    private static final int DEFAULT_QUANTITY = 1;

    private Member member1;
    private Member member2;
    private Product product;

    @BeforeEach
    void setUp() {
        member1 = TestFixture.getMember1();
        member2 = TestFixture.getMember2();
        product = TestFixture.PRODUCT1;
    }

    @DisplayName("장바구니 상품 소유자와 멤버 id가 다를 경우 Exception을 반환한다.")
    @Test
    void checkOwnerTest() {
        final CartItem cartItem = new CartItem(member1, product, DEFAULT_QUANTITY);

        assertThrows(CartItemException.IllegalMember.class, () -> cartItem.checkOwner(member2));
    }
}
