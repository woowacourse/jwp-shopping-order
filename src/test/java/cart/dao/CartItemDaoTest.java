package cart.dao;

import cart.domain.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

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
    void findByIds_success() {
        List<CartItem> items = cartItemDao.findItemsByIds(List.of(1L, 3L));

        List<Long> resultIds = items.stream().map(CartItem::getId).collect(Collectors.toList());
        assertThat(resultIds).containsExactly(1L, 3L);
    }

    @Test
    void deleteByIds_success() {
        cartItemDao.deleteByIds(List.of(1L, 2L));

        assertThat(cartItemDao.findById(1L)).isNull();
        assertThat(cartItemDao.findById(2L)).isNull();
    }
}
