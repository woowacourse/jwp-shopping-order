package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.PointHistoryDao;
import cart.domain.*;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.entity.PointHistoryEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static cart.ProductFixture.product1;
import static cart.ProductFixture.product2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql({"classpath:test_init.sql"})
@ActiveProfiles("test")
class OrderRepositoryTest {

    private JdbcTemplate jdbcTemplate;
    private OrderRepository orderRepository;
    private OrderDao orderDao;
    private OrderItemDao orderItemDao;
    private PointHistoryDao pointHistoryDao;

    private Member member;
    private Points points;

    @Autowired
    public OrderRepositoryTest(JdbcTemplate jdbcTemplate, OrderRepository orderRepository, OrderDao orderDao, OrderItemDao orderItemDao, PointHistoryDao pointHistoryDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderRepository = orderRepository;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.pointHistoryDao = pointHistoryDao;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into product (name, price, image_url) values ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.update("insert into product (name, price, image_url) values ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.update("insert into product (name, price, image_url) values ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80')");

        jdbcTemplate.update("insert into member(email, password) values('konghana@com', '1234')");

        jdbcTemplate.update("insert into orders(member_id, orders_status_id) values(1, 1)");

        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(1, 1, 3, 30000)");
        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(1, 2, 2, 40000)");

        jdbcTemplate.update("insert into point(member_id, orders_id, earned_point, comment, create_at, expired_at) values(1, 1, 5600, '주문 포인트 적립', '2023-06-02', '2023-09-30')");

        jdbcTemplate.update("insert into point_history(orders_id, point_id, used_point) values(1, 1, 1000)");

        member = new Member(1L, "kong@com", "1234");
        points = new Points(List.of(Point.of(1L, 4000, "주문 포인트 적립", LocalDate.of(2023, 06, 02), LocalDate.of(2023, 9, 30))));
    }

    @DisplayName("주문 정보를 저장할 수 있다.")
    @Test
    void save() {
        OrderItem orderItem1 = new OrderItem(product1, 2, 20000);
        OrderItem orderItem2 = new OrderItem(product2, 3, 60000);
        OrderItemEntity orderItemEntity1 = new OrderItemEntity(2L, product1, 2, 20000);
        OrderItemEntity orderItemEntity2 = new OrderItemEntity(2L, product2, 3, 60000);

        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);
        Order order = new Order(points, orderItems, member);

        orderRepository.save(order);

        OrderEntity orderEntityDb = orderDao.findById(2L);
        List<OrderItemEntity> orderItemsDb = orderItemDao.findByOrderId(2L);
        List<PointHistoryEntity> pointHistoryEntities = pointHistoryDao.findByPointIds(List.of(1L));

        assertAll(
                () -> assertThat(orderEntityDb.getId()).isEqualTo(2),
                () -> assertThat(orderItemsDb).containsExactlyInAnyOrder(orderItemEntity1, orderItemEntity2),
                () -> assertThat(pointHistoryEntities.size()).isEqualTo(2),
                () -> assertThat(pointHistoryEntities.get(0).getUsedPoint()).isEqualTo(1000),
                () -> assertThat(pointHistoryEntities.get(1).getUsedPoint()).isEqualTo(4000)
        );
    }

    @DisplayName("사용자에 대한 주문 정보를 반환할 수 있다.")
    @Test
    void findAllByMember() {
        OrderItem orderItem1 = new OrderItem(product1, 3, 30000);
        OrderItem orderItem2 = new OrderItem(product2, 2, 40000);
        List<Order> orders = orderRepository.findAllByMember(member);

        assertAll(
                () -> assertThat(orders.size()).isEqualTo(1),
                () -> assertThat(orders.get(0).getId()).isEqualTo(1L),
                () -> assertThat(orders.get(0).getOrderItems()).containsExactlyInAnyOrder(orderItem1, orderItem2),
                () -> assertThat(orders.get(0).getTotalUsedPoint()).isEqualTo(1000)
        );
    }
}
