package cart.dao;

import cart.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JdbcTest
@Sql("classpath:schema.sql")
class OrderDaoTest {

    OrderDao orderDao;


    private OrderDaoTest(@Autowired final JdbcTemplate jdbcTemplate) {
        this.orderDao = new OrderDao(jdbcTemplate);
    }

    @DisplayName("주문 저장")
    @Test
    void insert() {
        // when & then
        assertThat(orderDao.insert(Fixture.order1)).isPositive();
    }
}
