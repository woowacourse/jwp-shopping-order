package cart.dao;

import cart.domain.OrderStatus;
import cart.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@ActiveProfiles("test")
class OrderDaoTest {

    private final OrderDao orderDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    OrderDaoTest(OrderDao orderDao, JdbcTemplate jdbcTemplate) {
        this.orderDao = orderDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into orders(member_id, orders_status_id) values(1, 1)");

    }

    @DisplayName("주문 정보를 조회할 수 있다.")
    @Test
    void findById() {
        OrderEntity order = orderDao.findById(1L);

        assertAll(
                () -> assertThat(order.getMemberId()).isEqualTo(1L),
                () -> assertThat(order.getOrderStatusId()).isEqualTo(OrderStatus.PENDING.getOrderStatusId())
        );
    }
}
