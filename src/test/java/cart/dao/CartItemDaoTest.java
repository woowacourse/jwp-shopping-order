package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.CartItem;
import cart.fixture.Fixture;

class CartItemDaoTest extends DaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @Test
    @DisplayName("여러개의 id를 통해 cart에 담긴 상품을 삭제한다.")
    void deleteByIds() {
        //given
        final Long id1 = cartItemDao.save(Fixture.CART_ITEM1);
        final Long id2 = cartItemDao.save(Fixture.CART_ITEM2);
        final List<Long> ids = List.of(id1, id2);

        //when
        final int result = cartItemDao.deleteByIds(ids);

        //then
        assertThat(result).isEqualTo(ids.size());
    }

    @Test
    @DisplayName("여러개의 id를 통해 일치하는 모든 상품을 반환한다.")
    void findByIds() {
        //given
        final List<Long> cartItemIds = List.of(1L, 2L);

        //when
        final List<CartItem> result = cartItemDao.findByIds(cartItemIds);

        //then
        assertThat(result.size()).isEqualTo(cartItemIds.size());
    }
}
