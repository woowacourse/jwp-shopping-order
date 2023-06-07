package cart.cartitem.dao;

import cart.cartitem.domain.CartItem;
import cart.config.DaoTest;
import cart.fixtures.CartItemFixtures;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static cart.fixtures.CartItemFixtures.*;
import static cart.fixtures.CartItemFixtures.Member_Dooly_CartItem1;
import static cart.fixtures.MemberFixtures.Member_Dooly;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static cart.fixtures.ProductFixtures.PIZZA;
import static cart.fixtures.ProductFixtures.SALAD;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("NonAsciiCharacters")
class CartItemDaoTest extends DaoTest {

    @Test
    void 특정_멤버에_대한_장바구니_목록을_조회하다() {
        final List<CartItem> cartItems = cartItemDao.findByMemberId(Member_Dooly.ID);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cartItems).hasSize(2);
            softAssertions.assertThat(cartItems.get(0)).usingRecursiveComparison().isEqualTo(Member_Dooly_CartItem2.ENTITY);
            softAssertions.assertThat(cartItems.get(1)).usingRecursiveComparison().isEqualTo(Member_Dooly_CartItem1.ENTITY);
        });
    }

    @Test
    void 특정_멤버가_담아둔_특정_장바구니를_조회하다() {
        final Optional<CartItem> cartItem = cartItemDao.findByMemberIdAndProductId(1L, 1L);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cartItem.isPresent()).isTrue();
            softAssertions.assertThat(cartItem.get()).usingRecursiveComparison().isEqualTo(Member_Dooly_CartItem1.ENTITY);
        });
    }

    @Test
    void 장바구니_ID를_통해_장바구니를_조회하다() {
        final CartItem cartItem = cartItemDao.findById(1L);

        assertThat(cartItem).usingRecursiveComparison().isEqualTo(Member_Dooly_CartItem1.ENTITY);
    }

    @Test
    void 장바구니를_저장하다() {
        // given
        final CartItem cartItem = CartItem.of(null, 10, PIZZA.ENTITY, Member_Dooly.ENTITY);

        // when
        final Long cartItemId = cartItemDao.save(cartItem);

        // then
        assertThat(cartItemId).isEqualTo(7L);
    }

    @Test
    void 장바구니를_삭제하다() {
        assertDoesNotThrow(() -> cartItemDao.deleteById(1L));
    }

    @Test
    void 장바구니_상품의_개수를_수정하다() {
        assertDoesNotThrow(() -> cartItemDao.updateQuantity(Member_Dooly_CartItem1.ENTITY));
    }

    @Test
    void 장바구니_ID를_통해_장바구니가_몇_개_존재하는지_확인하다() {
        final Long cartItemCount = cartItemDao.countById(1L);

        assertThat(cartItemCount).isEqualTo(1L);
    }
}
