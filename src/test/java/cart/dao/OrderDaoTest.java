package cart.dao;

import static cart.fixtures.OrderFixtures.Dooly_Order1;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        this.orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    @DisplayName("받은 주문을 삽입한다.")
    void insert() {
        // given
        Order order = Dooly_Order1.DOMAIN();

        // when
        Order insertedOrder = orderDao.insert(order);

        // then
        assertThat(insertedOrder).isNotNull();
    }
}
