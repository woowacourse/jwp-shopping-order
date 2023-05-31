package cart.domain.cartitem;

import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static cart.fixtures.CartItemFixtures.MemberA_CartItem1;
import static cart.fixtures.MemberFixtures.MemberA;
import static cart.fixtures.MemberFixtures.MemberB;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CartItemTest {

    @Nested
    class checkOwner_테스트 {

        @Test
        void 멤버가_가진_장바구니가_맞으면_True를_반환하다() {
            assertDoesNotThrow(() -> MemberA_CartItem1.ENTITY.checkOwner(MemberA.ENTITY));
        }

        @Test
        void 멤버가_가진_장바구니가_아니라면_예외를_반환하다() {
            assertThatThrownBy(() -> MemberA_CartItem1.ENTITY.checkOwner(MemberB.ENTITY))
                    .isInstanceOf(CartItemException.class);
        }
    }
}
