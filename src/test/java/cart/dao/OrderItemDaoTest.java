package cart.dao;

import cart.domain.OrderItem;
import cart.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OrderItemDaoTest {

    private JdbcTemplate jdbcTemplate;
    private OrderItemDao orderItemDao;
    private DataSource dataSource;

    @Autowired
    public OrderItemDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into orders(member_id, used_point) values(1, 1500)");

        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(1, 1, 3, 30000)");
        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(1, 2, 2, 40000)");
    }

    @DisplayName("주문 정보를 조회할 수 있다.")
    @Test
    void findAll() {
        List<OrderItem> orderItems = orderItemDao.findAll();

        Product product1 = new Product(1L, "치킨", 10000, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
        Product product2 = new Product(2L, "샐러드", 20000, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");

        assertThat(orderItems).containsExactlyInAnyOrder(new OrderItem(product1, 3, 30000), new OrderItem(product2, 2, 40000));
    }
}
