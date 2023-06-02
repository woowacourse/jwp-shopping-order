package cart.domain.cartitem;

import cart.fixture.MemberFixture;
import cart.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemTest {

    @DisplayName("장바구니의 소유자가 아니면 true")
    @Test
    void isNotOwnedByMemberTrueTest() {
        CartItem cartItemDino = new CartItem(2, ProductFixture.꼬리요리_ID포함, MemberFixture.디노_ID포함);
        assertThat(cartItemDino.isNotOwnedByMember(MemberFixture.비버_ID포함)).isTrue();
    }

    @DisplayName("장바구니의 소유자면 false")
    @Test
    void isNotOwnedByMemberFalseTest() {
        CartItem cartItemDino = new CartItem(2, ProductFixture.꼬리요리_ID포함, MemberFixture.디노_ID포함);
        assertThat(cartItemDino.isNotOwnedByMember(MemberFixture.디노_ID포함)).isFalse();
    }

}
