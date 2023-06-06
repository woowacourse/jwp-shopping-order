package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import cart.dao.OrderDetailDao;
import cart.dao.OrdersDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Price;
import cart.entity.OrderDetailEntity;
import cart.entity.OrdersEntity;
import cart.exception.OrderNotFoundException;
import cart.exception.OrderOwnerException;
import cart.fixture.CartItemsFixture;
import cart.fixture.MemberFixture;
import cart.fixture.OrderFixture;
import cart.fixture.OrderPointFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

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
        final Long id = orderRepository.createOrder(MemberFixture.MEMBER, OrderFixture.ORDER1).getId();
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

    @Sql({"classpath:deleteAll.sql", "classpath:insertOrders.sql", "classpath:insertOrderDetail.sql"})
    @Test
    void findByMember() {
        final List<Order> result = orderRepository.findByMember(MemberFixture.MEMBER);
        assertAll(
                () -> assertThat(result).hasSize(2),

                () -> assertThat(result.get(0).getId()).isEqualTo(2L),
                () -> assertThat(result.get(0).getCartItems()).usingRecursiveComparison().isEqualTo(CartItemsFixture.ORDER2_CARTITEMS),
                () -> assertThat(result.get(0).getCartItems().getTotalPrice()).isEqualTo(Price.valueOf(235_000)),
                () -> assertThat(result.get(0).getOrderPoint()).usingRecursiveComparison().isEqualTo(OrderPointFixture.ORDER_POINT2),
                () -> assertThat(result.get(0).getCreatedAt()).isEqualTo(Timestamp.valueOf("2024-05-31 10:00:00")),

                () -> assertThat(result.get(1).getId()).isEqualTo(1L),
                () -> assertThat(result.get(1).getCartItems()).usingRecursiveComparison().isEqualTo(CartItemsFixture.ORDER1_CARTITEMS),
                () -> assertThat(result.get(1).getCartItems().getTotalPrice()).isEqualTo(Price.valueOf(65_000)),
                () -> assertThat(result.get(1).getOrderPoint()).usingRecursiveComparison().isEqualTo(OrderPointFixture.ORDER_POINT1),
                () -> assertThat(result.get(1).getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertOrders.sql", "classpath:insertOrderDetail.sql"})
    @Test
    void findByNoExistId() {
        assertThatThrownBy(() -> orderRepository.findById(MemberFixture.MEMBER, 3L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage(new OrderNotFoundException(3L).getMessage());
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertOrders.sql", "classpath:insertOrderDetail.sql"})
    @Test
    void findByIdNotMine() {
        final Member invalidMember = new Member(2L, null, null);
        assertThatThrownBy(() -> orderRepository.findById(invalidMember, 1L))
                .isInstanceOf(OrderOwnerException.class)
                .hasMessage(new OrderOwnerException(1L).getMessage());
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertOrders.sql", "classpath:insertOrderDetail.sql"})
    @Test
    void findById() {
        final Order result = orderRepository.findById(MemberFixture.MEMBER, 1L);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getCartItems()).usingRecursiveComparison().isEqualTo(CartItemsFixture.ORDER1_CARTITEMS),
                () -> assertThat(result.getCartItems().getTotalPrice()).isEqualTo(Price.valueOf(65_000)),
                () -> assertThat(result.getOrderPoint()).usingRecursiveComparison().isEqualTo(OrderPointFixture.ORDER_POINT1),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }
}
