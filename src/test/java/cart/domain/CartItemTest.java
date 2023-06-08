package cart.domain;

import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cart.fixtures.CartItemFixtures.CART_ITEM1;
import static cart.fixtures.MemberFixtures.MEMBER2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartItemTest {

    @Test
    @DisplayName("장바구니의 주인이 아니면 예외가 발생한다.")
    void checkOwnerTest() {
        // given
        Member wrongMember = MEMBER2;
        CartItem cartItem = CART_ITEM1;

        // when, then
        assertThatThrownBy(() -> cartItem.checkOwner(wrongMember))
                .isInstanceOf(CartItemException.IllegalMember.class);
    }

    @Test
    @DisplayName("상품의 개수를 수정한다.")
    void changeQuantityTest() {
        // given
        int newQuantity = 2;
        CartItem cartItem = CART_ITEM1;

        // when
        cartItem.changeQuantity(newQuantity);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(newQuantity);
    }
}