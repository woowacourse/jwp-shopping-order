package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import cart.domain.CartItem;
import cart.fixture.CartItemFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
class CartItemDaoTest {

    private CartItemDao cartItemDao;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate) {
        cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void insert() {
        final CartItem result = cartItemDao.insert(CartItemFixture.CHICKEN);
        assertAll(
                () -> assertThat(result.getId()).isPositive(),
                () -> assertThat(result.getMember().getId()).isEqualTo(1L),
                () -> assertThat(result.getMember().getEmail()).isEqualTo("odo1@woowa.com"),
                () -> assertThat(result.getMember().getPassword()).isEqualTo("1234"),
                () -> assertThat(result.getProduct().getId()).isEqualTo(1L),
                () -> assertThat(result.getProduct().getName()).isEqualTo("치킨"),
                () -> assertThat(result.getProduct().getPrice()).isEqualTo(10_000),
                () -> assertThat(result.getProduct().getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.getQuantity()).isEqualTo(1)
        );
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void findByMemberId() {
        final Long chickenId = cartItemDao.insert(CartItemFixture.CHICKEN).getId();
        final Long pizzaId = cartItemDao.insert(CartItemFixture.PIZZA).getId();
        final List<CartItem> result = cartItemDao.findByMemberId(1L);
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(chickenId),
                () -> assertThat(result.get(0).getMember().getId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getProduct().getId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getQuantity()).isEqualTo(1),
                () -> assertThat(result.get(1).getId()).isEqualTo(pizzaId),
                () -> assertThat(result.get(1).getMember().getId()).isEqualTo(1L),
                () -> assertThat(result.get(1).getProduct().getId()).isEqualTo(2L),
                () -> assertThat(result.get(1).getQuantity()).isEqualTo(1)
        );
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void findById() {
        final Long id = cartItemDao.insert(CartItemFixture.CHICKEN).getId();
        final Optional<CartItem> result = cartItemDao.findById(id);
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getId()).isEqualTo(id),
                () -> assertThat(result.get().getMember().getId()).isEqualTo(1L),
                () -> assertThat(result.get().getProduct().getId()).isEqualTo(1L),
                () -> assertThat(result.get().getQuantity()).isEqualTo(1)
        );
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void findByNoExistId() {
        final Long id = cartItemDao.insert(CartItemFixture.CHICKEN).getId();
        cartItemDao.deleteById(id);
        final Optional<CartItem> result = cartItemDao.findById(id);
        assertThat(result).isEmpty();
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void updateQuantity() {
        final Long id = cartItemDao.insert(CartItemFixture.CHICKEN).getId();
        cartItemDao.updateQuantity(new CartItem(id, null, null, 10));
        final Optional<CartItem> result = cartItemDao.findById(id);
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getId()).isEqualTo(id),
                () -> assertThat(result.get().getMember().getId()).isEqualTo(1L),
                () -> assertThat(result.get().getProduct().getId()).isEqualTo(1L),
                () -> assertThat(result.get().getQuantity()).isEqualTo(10)
        );
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void deleteById() {
        final Long id = cartItemDao.insert(CartItemFixture.CHICKEN).getId();
        cartItemDao.deleteById(id);
        final Optional<CartItem> result = cartItemDao.findById(id);
        assertThat(result).isEmpty();
    }
}
