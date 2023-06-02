package cart.dao;

import cart.domain.OrderStatus;
import cart.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@ActiveProfiles("test")
@Sql(value = {"classpath:test_init.sql"})
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
        jdbcTemplate.update("insert into product (name, price, image_url) values ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.update("insert into product (name, price, image_url) values ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.update("insert into product (name, price, image_url) values ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80')");

        jdbcTemplate.update("insert into member(email, password) values('konghana@com', '1234')");
        jdbcTemplate.update("insert into member(email, password) values('kong@com', '1234567')");

        jdbcTemplate.update("insert into orders(member_id, orders_status_id) values(1, 1)");
    }

    @DisplayName("주문 정보를 조회할 수 있다.")
    @Test
    void findById() {
        OrderEntity order = orderDao.findById(1L);

        assertAll(
                () -> assertThat(order.getId()).isEqualTo(1L),
                () -> assertThat(order.getMemberId()).isEqualTo(1L),
                () -> assertThat(order.getOrderStatusId()).isEqualTo(OrderStatus.PENDING.getOrderStatusId())
        );
    }

    @DisplayName("주문 정보를 저장할 수 있다.")
    @Test
    void save() {
        OrderEntity orderEntity = new OrderEntity(2L, 1);

        orderDao.save(orderEntity);

        OrderEntity order = orderDao.findById(2L);

        assertAll(
                () -> assertThat(order.getId()).isEqualTo(2L),
                () -> assertThat(order.getMemberId()).isEqualTo(2L),
                () -> assertThat(order.getOrderStatusId()).isEqualTo(OrderStatus.PENDING.getOrderStatusId())
        );
    }
}
