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
class OrderItemDaoTest {

    OrderDao orderDao;
    OrderItemDao orderItemDao;

    private OrderItemDaoTest(@Autowired final JdbcTemplate jdbcTemplate) {
        this.orderDao = new OrderDao(jdbcTemplate);
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    @DisplayName("주문 상품 저장")
    @Test
    void insert() {
        // given
        final Long orderId = orderDao.insert(Fixture.order1);

        // when & then
        assertThat(orderItemDao.insert(orderId, Fixture.orderItem1)).isPositive();
    }
}
