package cart.domain;

import static cart.fixture.DomainFixture.MEMBER_A;
import static cart.fixture.DomainFixture.MEMBER_B;
import static cart.fixture.DomainFixture.TWO_CHICKEN;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartItemTest {

    @Test
    @DisplayName("checkOwner는 해당 장바구니 상품을 등록한 회원을 전달하면 정상적으로 동작한다.")
    void checkOwnerSuccessTest() {
        assertDoesNotThrow(() -> TWO_CHICKEN.checkOwner(MEMBER_A));
    }

    @Test
    @DisplayName("checkOwner는 해당 장바구니 상품을 등록한 회원이 아닌 다른 회원을 전달하면 예외가 발생한다.")
    void checkOwnerFailTest() {
        assertThatThrownBy(() -> TWO_CHICKEN.checkOwner(MEMBER_B))
                .isInstanceOf(CartItemException.IllegalMember.class)
                .hasMessage("해당 사용자의 장바구니 상품이 아닙니다.");
    }

    @Test
    @DisplayName("changeQuantity는 변경할 수량으로 0 이상을 전달하면 정상 동작한다.")
    void changeQuantitySuccessTest() {
        assertDoesNotThrow(() -> TWO_CHICKEN.changeQuantity(0));
    }

    @Test
    @DisplayName("changeQuantity는 변경할 수량으로 음수를 전달하면 예외가 발생한다.")
    void changeQuantityFailTest() {
        assertThatThrownBy(() -> TWO_CHICKEN.changeQuantity(-1))
                .isInstanceOf(CartItemException.InvalidQuantity.class)
                .hasMessage("수량은 음수일 수 없습니다.");
    }
}
