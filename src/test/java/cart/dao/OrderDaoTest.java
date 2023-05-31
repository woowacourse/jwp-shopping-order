package cart.dao;

import cart.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThatCode;

@JdbcTest
@DisplayName("Order Dao 테스트")
@Sql("/data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderDaoTest {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        orderDao = new OrderDao(dataSource);
    }

    @Test
    @DisplayName("저장 성공")
    void insert_success() {
        // given
        final OrderEntity orderEntity = new OrderEntity(1L, 15_000, 0);

        // when, then
        assertThatCode(() -> orderDao.insert(orderEntity))
                .doesNotThrowAnyException();
    }
}
