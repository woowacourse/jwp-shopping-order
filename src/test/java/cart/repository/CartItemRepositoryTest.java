package cart.repository;

import static fixture.CartItemFixture.CART_ITEM_1;
import static fixture.CartItemFixture.CART_ITEM_2;
import static fixture.CartItemFixture.CART_ITEM_3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import anotation.RepositoryTest;
import cart.dao.CartItemDao;
import cart.domain.CartItem;
import fixture.MemberFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartItemDao cartItemDao;


    @Test
    @DisplayName("id 들을 가지고 CartItem 들을 조회한다.")
    void findCartItemByIds() {
        List<CartItem> cartItemByIds = cartItemRepository.findCartItemsByIds(List.of(1L, 2L, 3L));

        assertThat(cartItemByIds)
                .usingRecursiveComparison()
                .ignoringFields("member.password")
                .isEqualTo(List.of(CART_ITEM_1, CART_ITEM_2, CART_ITEM_3));
    }

    @Test
    @DisplayName("CartItems 에 들어있는 물품들을 삭제한다. (성공)")
    void delete_success() {
        cartItemRepository.deleteCartItems(List.of(1L, 2L));

        List<CartItem> cartItemsAfterDelete = cartItemDao.findByMemberId(MemberFixture.MEMBER_1.getId());

        assertThat(cartItemsAfterDelete).isEmpty();
    }

    @Test
    @DisplayName("CartItems 에 들어있는 물품들을 삭제한다. (실패)")
    void delete_fail() {
        List<Long> removeCartItemIds = List.of(100L, 101L, 102L);

        assertThatThrownBy(() -> cartItemRepository.deleteCartItems(removeCartItemIds))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
