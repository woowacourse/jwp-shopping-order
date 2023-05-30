package cart.dao;

import cart.domain.CartItem;
import cart.fixtures.CartItemFixtures;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixtures.MemberFixtures.MemberA;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static cart.fixtures.ProductFixtures.SALAD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("NonAsciiCharacters")
class CartItemDaoTest extends DaoTest {

    @Test
    void 멤버_id를_통해_장바구니를_찾는다() {
        final List<CartItem> cartItems = cartItemDao.findByMemberId(1L);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cartItems).hasSize(2);
            softAssertions.assertThat(cartItems.get(0).getId()).isEqualTo(1L);
            softAssertions.assertThat(cartItems.get(0).getMember()).isEqualTo(MemberA.ENTITY);
            softAssertions.assertThat(cartItems.get(0).getProduct()).isEqualTo(CHICKEN.ENTITY);
            softAssertions.assertThat(cartItems.get(0).getQuantity()).isEqualTo(2);
            softAssertions.assertThat(cartItems.get(1).getId()).isEqualTo(2L);
            softAssertions.assertThat(cartItems.get(1).getMember()).isEqualTo(MemberA.ENTITY);
            softAssertions.assertThat(cartItems.get(1).getProduct()).isEqualTo(SALAD.ENTITY);
            softAssertions.assertThat(cartItems.get(1).getQuantity()).isEqualTo(4);
        });
    }

    @Test
    void 멤버_id와_상품_id를_통해_장바구니를_찾는다() {
        final CartItem cartItem = cartItemDao.findByMemberIdAndProductId(1L, 1L);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cartItem.getId()).isEqualTo(1L);
            softAssertions.assertThat(cartItem.getMember()).isEqualTo(MemberA.ENTITY);
            softAssertions.assertThat(cartItem.getProduct()).isEqualTo(CHICKEN.ENTITY);
            softAssertions.assertThat(cartItem.getQuantity()).isEqualTo(2);
        });
    }

    @Test
    void 장바구니를_저장하다() {
        final CartItem cartItem = CartItemFixtures.MemberA_CartItem1.ENTITY;

        assertThat(cartItemDao.save(cartItem)).isEqualTo(7L);
    }

    @Test
    void 장바구니_id를_통해_장바구니를_찾는다() {
        final CartItem cartItem = cartItemDao.findById(1L);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cartItem.getId()).isEqualTo(1L);
            softAssertions.assertThat(cartItem.getMember()).isEqualTo(MemberA.ENTITY);
            softAssertions.assertThat(cartItem.getProduct()).isEqualTo(CHICKEN.ENTITY);
            softAssertions.assertThat(cartItem.getQuantity()).isEqualTo(2);
        });
    }

    @Test
    void 멤버_id와_상품_id를_통해_장바구니를_삭제한다() {
        assertDoesNotThrow(() -> cartItemDao.delete(1L, 1L));
    }

    @Test
    void 장바구니_id를_통해_장바구니를_삭제한다() {
        assertDoesNotThrow(() -> cartItemDao.deleteById(1L));
    }

    @Test
    void 장바구니_물건의_수량을_수정하다() {
        final CartItem cartItem = CartItem.of(1L, 10, CHICKEN.ENTITY, MemberA.ENTITY);

        assertDoesNotThrow(() -> cartItemDao.updateQuantity(cartItem));
    }
}
