package cart.cartitem.dao;

import cart.cartitem.repository.CartItemEntity;
import cart.init.DBInit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class CartItemDaoTest extends DBInit {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private CartItemDao cartItemDao;
    
    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);
    }
    
    @Test
    void memberId로_장바구니_조회하기() {
        // when
        final List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(1L);
        
        // then
        assertThat(cartItemEntities).hasSize(3);
    }

    @Test
    void 장바구니_담기() {
        // given
        final CartItemEntity cartItemEntity = new CartItemEntity(null, 1L, 2L, 5L);

        // when
        final Long cartItemId = cartItemDao.save(cartItemEntity);

        // then
        assertThat(cartItemId).isPositive();
    }

    @Test
    void CartItemId로_CartItem_조회하기() {
        // given
        final CartItemEntity cartItemEntity = new CartItemEntity(null, 1L, 2L, 5L);
        final Long cartItemId = cartItemDao.save(cartItemEntity);

        // when
        final CartItemEntity actualEntity = cartItemDao.findById(cartItemId);

        // then
        assertThat(actualEntity).isEqualTo(new CartItemEntity(cartItemId, 1L, 2L, 5L));
    }

    @Test
    void 장바구니를_삭제한다() {
        // expect
        assertThatNoException()
                .isThrownBy(() -> cartItemDao.delete(1L, 1L));
    }

    @Test
    void 장바구니를_수정한다() {
        // expect
        assertThatNoException()
                .isThrownBy(() -> cartItemDao.update(new CartItemEntity(null, 1L, 2L, 4L)));
    }

    @Test
    void cartItemId로_장바구니를_삭제한다() {
        // expect
        assertThatNoException()
                .isThrownBy(() -> cartItemDao.delete(1L, 2L));
    }

    @Test
    void productId로_장바구니를_삭제한다() {
        // expect
        assertThatNoException()
                .isThrownBy(() -> cartItemDao.deleteByProductId(1L));
    }
}
