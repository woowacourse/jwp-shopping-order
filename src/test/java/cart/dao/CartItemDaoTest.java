package cart.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CartItemDaoTest {

    private final CartItemDao cartItemDao;

    @Autowired
    public CartItemDaoTest(final JdbcTemplate jdbcTemplate) {
        this.cartItemDao = new CartItemDao(jdbcTemplate, new NamedParameterJdbcTemplate(jdbcTemplate));
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    void findByMemberId() {
        // given
        cartItemDao.findByMemberId(1L);

        // when

        // then
    }

    @Test
    void save() {
    }

    @Test
    void findById() {
    }

    @Test
    void isExist() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void deleteByIds() {
    }

    @Test
    void updateQuantity() {
    }
}
