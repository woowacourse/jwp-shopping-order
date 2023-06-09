package cart.repository;

import static fixture.CartItemFixture.장바구니_유저_1_치킨_2개;
import static fixture.CartItemFixture.장바구니_유저_1_샐러드_4개;
import static fixture.CartItemFixture.장바구니_유저_2_피자_5개;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import anotation.RepositoryTest;
import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.exception.CartNotFoundException;
import cart.exception.DeleteFailException;
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
        List<CartItem> cartItemByIds = cartItemRepository.findCartItemsByIds(List.of(장바구니_유저_1_치킨_2개.getId(), 장바구니_유저_1_샐러드_4개.getId(), 장바구니_유저_2_피자_5개.getId()));

        assertThat(cartItemByIds)
                .usingRecursiveComparison()
                .ignoringFields("member.password")
                .isEqualTo(List.of(장바구니_유저_1_치킨_2개, 장바구니_유저_1_샐러드_4개, 장바구니_유저_2_피자_5개));
    }

    @Test
    @DisplayName("CartItems 에 들어있는 물품들을 삭제한다. (성공)")
    void delete_success() {
        cartItemRepository.deleteCartItems(List.of(장바구니_유저_1_치킨_2개.getId(), 장바구니_유저_1_샐러드_4개.getId()));

        List<CartItem> cartItemsAfterDelete = cartItemDao.findByMemberId(MemberFixture.유저_1.getId());

        assertThat(cartItemsAfterDelete).isEmpty();
    }

    @Test
    @DisplayName("CartItems 에 들어있는 물품들을 삭제한다. (실패)")
    void delete_fail() {
        List<Long> removeCartItemIds = List.of(100L, 101L, 102L);

        assertThatThrownBy(() -> cartItemRepository.deleteCartItems(removeCartItemIds))
                .isInstanceOf(DeleteFailException.class);
    }

}
