package cart.dao;

import cart.dao.entity.CartItemEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class CartItemDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @Test
    void 저장한다() {
        CartItemEntity cartItemEntity = new CartItemEntity(null, 1L, 1L, 2);

        Long generatedId = cartItemDao.save(cartItemEntity);

        assertThat(generatedId).isNotNull();
    }

    @Test
    void 멤버_아이디로_조회한다() {
        Long memberId = 1L;

        List<CartItemEntity> cartItems = cartItemDao.findByMemberId(memberId);

        assertAll(
            () -> assertThat(cartItems.get(0).getMemberId()).isEqualTo(memberId),
            () -> assertThat(cartItems.get(1).getMemberId()).isEqualTo(memberId)
        );
    }

    @Test
    void 아이디_목록으로_조회한다(){
        List<Long> ids = List.of(1L, 2L);

        List<CartItemEntity> cartItems = cartItemDao.findByIds(ids);

        assertAll(
            () -> assertThat(cartItems.get(0).getId()).isEqualTo(1L),
            () -> assertThat(cartItems.get(1).getId()).isEqualTo(2L)
        );
    }

    @Test
    void 아이디로_조회한다() {
        Long id = 1L;

        // When
        Optional<CartItemEntity> cartItem = cartItemDao.findById(id);

        assertThat(cartItem.get().getId()).isEqualTo(id);
    }

    @Test
    void 아이디로_조회_시_없으면_빈_값_반환한다() {
        Long nonExistingId = 100L;

        Optional<CartItemEntity> cartItem = cartItemDao.findById(nonExistingId);

        assertThat(cartItem).isEmpty();
    }

    @Test
    void 아이디_목록으로_삭제한다() {
        List<Long> ids = List.of(1L, 2L);

        cartItemDao.deleteAllByIds(ids);

        List<CartItemEntity> cartItems = cartItemDao.findByIds(ids);
        assertThat(cartItems).isEmpty();
    }

    @Test
    void 수정한다() {
        Long id = 1L;
        CartItemEntity cartItemEntity = new CartItemEntity(id, 1L, 1L, 5);

        cartItemDao.update(cartItemEntity);

        Optional<CartItemEntity> updatedCartItem = cartItemDao.findById(id);
        assertThat(updatedCartItem.get().getQuantity()).isEqualTo(5);
    }

    @Test
    void 삭제한다() {
        Long existingId = 1L;

        cartItemDao.deleteById(existingId);

        Optional<CartItemEntity> deletedCartItem = cartItemDao.findById(existingId);
        assertThat(deletedCartItem).isEmpty();
    }
}
