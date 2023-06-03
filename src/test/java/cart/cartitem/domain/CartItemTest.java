package cart.cartitem.domain;

import cart.cartitem.exception.CartItemException;
import cart.fixtures.ProductFixtures;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static cart.fixtures.CartItemFixtures.MemberA_CartItem1;
import static cart.fixtures.MemberFixtures.Member_Dooly;
import static cart.fixtures.MemberFixtures.Member_Ber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CartItemTest {

    @Test
    void 잘못된_유저가_장바구니_상품에_접근하면_예외를_반환하다() {

        assertThatThrownBy(() -> MemberA_CartItem1.ENTITY.checkOwner(Member_Ber.ENTITY))
                .isInstanceOf(CartItemException.IllegalMember.class);
    }

    @Test
    void 장바구니에_있는_상품의_수량을_변경하다() {
        // given
        final CartItem cartItem = CartItem.of(Member_Dooly.ENTITY, ProductFixtures.CHICKEN.ENTITY);

        // when
        cartItem.changeQuantity(5);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(5);
    }

    @Test
    void 장바구니에_있는_상품_수량을_추가하다() {
        // given
        final CartItem cartItem = CartItem.of(Member_Dooly.ENTITY, ProductFixtures.CHICKEN.ENTITY);

        // when
        cartItem.addQuantity(10);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(11);
    }
}
