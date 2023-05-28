package cart.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static cart.fixture.Fixture.ORDERS;


@JdbcTest
class OrdersDaoTest {
    private final OrdersDao ordersDao;
    @Autowired
    private OrdersDaoTest(JdbcTemplate jdbcTemplate){
        this.ordersDao = new OrdersDao(jdbcTemplate);
    }
    @Test
    @DisplayName("주문을 받는다")
    void createOrder(){
        Assertions.assertThat(ordersDao.createOrders(ORDERS)).isEqualTo(1L);
    }
}
