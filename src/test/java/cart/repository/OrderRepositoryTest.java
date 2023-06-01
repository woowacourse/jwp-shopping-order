package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import cart.dao.OrderDetailDao;
import cart.dao.OrdersDao;
import cart.entity.OrderDetailEntity;
import cart.entity.OrdersEntity;
import cart.fixture.MemberFixture;
import cart.fixture.OrderFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderRepositoryTest {

    private OrdersDao ordersDao;
    private OrderDetailDao orderDetailDao;
    private OrderRepository orderRepository;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate) {
        ordersDao = new OrdersDao(jdbcTemplate);
        orderDetailDao = new OrderDetailDao(jdbcTemplate);
        orderRepository = new OrderRepository(ordersDao, orderDetailDao);
    }

    @Test
    void createOrder() {
        final Long id = orderRepository.createOrder(MemberFixture.MEMBER, OrderFixture.ORDER).getId();
        final Optional<OrdersEntity> ordersEntity = ordersDao.findById(id);
        final List<OrderDetailEntity> orderDetailEntities = orderDetailDao.findByOrderId(id);
        assertAll(
                () -> assertThat(id).isPositive(),

                () -> assertThat(ordersEntity).isPresent(),
                () -> assertThat(ordersEntity.get().getId()).isEqualTo(id),
                () -> assertThat(ordersEntity.get().getMemberId()).isEqualTo(1L),
                () -> assertThat(ordersEntity.get().getPointId()).isEqualTo(1L),
                () -> assertThat(ordersEntity.get().getEarnedPoint()).isEqualTo(50),
                () -> assertThat(ordersEntity.get().getUsedPoint()).isEqualTo(100),
                () -> assertThat(ordersEntity.get().getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00")),

                () -> assertThat(orderDetailEntities.get(0).getId()).isPositive(),
                () -> assertThat(orderDetailEntities.get(0).getOrdersId()).isEqualTo(id),
                () -> assertThat(orderDetailEntities.get(0).getProductId()).isEqualTo(1L),
                () -> assertThat(orderDetailEntities.get(0).getProductName()).isEqualTo("치킨"),
                () -> assertThat(orderDetailEntities.get(0).getProductPrice()).isEqualTo(10_000),
                () -> assertThat(orderDetailEntities.get(0).getProductImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(orderDetailEntities.get(0).getOrderQuantity()).isEqualTo(1),
                () -> assertThat(orderDetailEntities.get(1).getId()).isPositive(),
                () -> assertThat(orderDetailEntities.get(1).getOrdersId()).isEqualTo(id),
                () -> assertThat(orderDetailEntities.get(1).getProductId()).isEqualTo(2L),
                () -> assertThat(orderDetailEntities.get(1).getProductName()).isEqualTo("피자"),
                () -> assertThat(orderDetailEntities.get(1).getProductPrice()).isEqualTo(15_000),
                () -> assertThat(orderDetailEntities.get(1).getProductImageUrl()).isEqualTo("http://example.com/pizza.jpg"),
                () -> assertThat(orderDetailEntities.get(1).getOrderQuantity()).isEqualTo(1)
        );
    }
}
