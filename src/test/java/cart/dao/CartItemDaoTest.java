package cart.dao;

import cart.domain.CartItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static cart.fixture.Fixture.*;

@JdbcTest
class CartItemDaoTest {
    private final CartItemDao cartItemDao;

    @Autowired
    public CartItemDaoTest(JdbcTemplate jdbcTemplate) {
        this.cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @Test
    @DisplayName("사용자 아이디로 장바구니를 찾는다.")
    void findByMemberId() {
        Assertions.assertThat(cartItemDao.findByMemberId(TEST_MEMBER.getId())).hasSize(3);
    }

    @Test
    @DisplayName("장바구니를 저장한다.")
    void save() {
        Assertions.assertThat(cartItemDao.save(CART_ITEM)).isEqualTo(5L);
    }

    @Test
    @DisplayName("장바구니 id로 장바구니를 찾는다")
    void findById() {
        Assertions.assertThat(cartItemDao.findById(1L).getProduct().getName()).isEqualTo("제네시스 g80");
    }

    @Test
    @DisplayName("장바구니 id로 지운다.")
    void delete() {
        cartItemDao.delete(TEST_MEMBER.getId(), 1L);
        Assertions.assertThat(cartItemDao.findById(1L)).isNull();
    }

    @Test
    @DisplayName("장바구니를 지운다.")
    void deleteById() {
        cartItemDao.deleteById(1L);
        Assertions.assertThat(cartItemDao.findById(1L)).isNull();

    }

    @Test
    @DisplayName("장바구니 수량을 수정한다.")
    void updateQuantity() {
        CartItem cartItem = new CartItem(1L, 10, PRODUCT, TEST_MEMBER);
        cartItemDao.updateQuantity(cartItem);
        Assertions.assertThat(cartItemDao.findById(1L).getQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("장바구니를 찾는다.")
    void findCartItemEntitiesByCartId() {
        Assertions.assertThat(cartItemDao.findCartItemEntitiesByCartId(1l).getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("장바구니 아이디를 찾는다.")
    void findProductIdByCartId() {
        Assertions.assertThat(cartItemDao.findProductIdByCartId(4L)).isEqualTo(3L);
    }
}
