package cart.dao;

import cart.domain.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static cart.ProductFixture.product1;
import static cart.ProductFixture.product2;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OrderItemDaoTest {

    private JdbcTemplate jdbcTemplate;
    private OrderItemDao orderItemDao;

    @Autowired
    public OrderItemDaoTest(JdbcTemplate jdbcTemplate) {
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into orders(member_id, orders_status_id) values(1, 1)");

        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(1, 1, 3, 30000)");
        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(1, 2, 2, 40000)");
    }

    @DisplayName("주문 상품 정보를 조회할 수 있다.")
    @Test
    void findAll() {
        List<OrderItem> orderItems = orderItemDao.findAll();

        assertThat(orderItems).containsExactlyInAnyOrder(new OrderItem(product1, 3, 30000), new OrderItem(product2, 2, 40000));
    }

    @DisplayName("주문 번호를 기준으로 주문 상품 정보를 조회할 수 있다.")
    @Test
    void findByOrderId() {
        List<OrderItem> orderItems = orderItemDao.findByOrderId(1L);

        assertThat(orderItems).containsAnyOf(new OrderItem(product1, 3, 30000), new OrderItem(product2, 2, 40000));
    }

    @DisplayName("주문 상품 정보를 추가할 수 있다.")
    @Test
    void saveAll() {
        OrderItem orderItem1 = new OrderItem(product1, 4, 40000);
        OrderItem orderItem2 = new OrderItem(product2, 3, 60000);

        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);
        orderItemDao.saveAll(1L, orderItems);

        List<OrderItem> actualOrderItems = orderItemDao.findAll();

        assertThat(actualOrderItems).containsAnyOf(orderItem1, orderItem2);
    }
}
