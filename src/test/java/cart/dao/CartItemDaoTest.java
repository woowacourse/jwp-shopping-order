package cart.dao;

import cart.domain.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
class CartItemDaoTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(namedParameterJdbcTemplate, jdbcTemplate);
    }

    @DisplayName("회원의 장바구니가 비어있는 경우 조회 시 빈 값을 반환한다")
    @Test
    void findEmtpyByMemberId() {
        // given
        Long memberId = 100L;

        // when
        List<CartItem> cartItems = cartItemDao.findByMemberId(memberId);

        // then
        assertThat(cartItems).hasSize(0);
    }

    @DisplayName("회원의 특정 장바구니가 존재하지 않는 경우 빈 값을 반환한다")
    @Test
    void findEmptyById() {
        // given
        Long id = 100L;

        // when
        CartItem cartItem = cartItemDao.findById(id);

        // then
        assertThat(cartItem).isNull();
    }

    @DisplayName("회원 아이디가 동일하고 상품 아이디 리스트에 포함된다면 제거한다")
    @Test
    void deleteByProductIds() {
        // given
        int originalCartItemSize = getAllCartItemSize();
        Long memberId = 1L;
        List<Long> productIds = List.of(1L, 3L);

        // when
        cartItemDao.deleteByProductIds(memberId, productIds);

        // then
        int afterDeleteCartItemSize = getAllCartItemSize();
        assertThat(afterDeleteCartItemSize).isEqualTo(originalCartItemSize - productIds.size());
    }

    private int getAllCartItemSize() {
        String sql = "SELECT COUNT(*) FROM cart_item";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
