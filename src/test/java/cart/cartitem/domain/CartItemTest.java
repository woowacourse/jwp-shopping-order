package cart.cartitem.domain;

import cart.exception.CartItemException;
import cart.member.domain.Member;
import org.junit.jupiter.api.Test;

import static cart.member.domain.MemberTest.*;
import static cart.product.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
public class CartItemTest {

    public static CartItem CART_ITEM = new CartItem(5L, 5L, PRODUCT_FIRST, MEMBER);

    @Test
    void 해당_회원의_장바구니가_아니면_예외처리() {
        // expect
        assertThatThrownBy(() -> CART_ITEM.checkOwner(new Member(2L, "b@b.com", "1234", 10000L)))
                .isInstanceOf(CartItemException.IllegalMember.class);
    }

    @Test
    void 장바구니_수량을_수정한다() {
        // given
        final CartItem cartItem = new CartItem(5L, 5L, PRODUCT_FIRST, MEMBER);

        // when
        cartItem.changeQuantity(10L);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(10L);
    }
}
