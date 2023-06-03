package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cart.CartItem;
import cart.repository.dao.CartItemDao;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @DisplayName("여러 ID를 입력받아 해당하는 row를 모두 조회한다")
    @Test
    void findByIds() {
        // given
        final List<Long> cartItemIds = List.of(1L, 2L, 5L);

        // when
        final List<CartItem> cartItems = cartItemDao.findByIds(cartItemIds);

        // then
        assertThat(cartItems).map(CartItem::getId)
                .isEqualTo(cartItemIds);
    }

    @DisplayName("존재하지 않는 id가 포함되어 있으면 해당 id의 장바구니 상품은 조회되지 않는다")
    @Test
    void findByIds_containsNonExistId_notFound() {
        // given
        final List<Long> cartItemIds = List.of(1L, 2L, Long.MAX_VALUE);

        // when
        final List<CartItem> cartItems = cartItemDao.findByIds(cartItemIds);

        // then
        assertThat(cartItems).hasSize(cartItemIds.size() - 1);
    }

    @DisplayName("여러 ID를 입력받아 해당하는 row를 모두 삭제한다")
    @Test
    void deleteByIds() {
        // given
        final List<Long> cartItemIds = List.of(1L, 2L, Long.MAX_VALUE);

        // when
        cartItemDao.deleteByIds(cartItemIds);
        final List<CartItem> findCartItems = cartItemDao.findByIds(cartItemIds);

        // then
        assertThat(findCartItems).isEmpty();
    }
}
