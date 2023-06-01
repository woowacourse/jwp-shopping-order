package cart.step2.order.persist;

import cart.step2.order.domain.Order;
import cart.step2.order.domain.OrderEntity;
import cart.step2.order.domain.OrderItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;
    private OrderItemDao orderItemDao;

    @BeforeEach
    void setUp() {
        this.orderDao = new OrderDao(jdbcTemplate);
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    @DisplayName("List<OrderItem>을 입력받고 한 번에 저장한다.")
    @Test
    void batchInsert() {
        // given
        final OrderEntity orderEntity = OrderEntity.createNonePkOrder(1000, 1L, 1L);
        orderDao.insert(orderEntity);

        final List<OrderItem> orderItems = List.of(
                OrderItem.createNonePkOrder(1L, 1L, 1),
                OrderItem.createNonePkOrder(2L, 1L, 1),
                OrderItem.createNonePkOrder(3L, 1L, 1)
        );

        // when
        orderItemDao.batchInsert(orderItems);
        List<OrderItem> responses = orderItemDao.findAll(1L);

        // then
        assertAll(
                () -> assertThat(responses).extracting(OrderItem::getProductId)
                        .contains(1L, 2L, 3L),
                () -> assertThat(responses).extracting(OrderItem::getOrderId)
                        .contains(1L, 1L, 1L),
                () -> assertThat(responses).extracting(OrderItem::getQuantity)
                        .contains(1, 1, 1)
        );
    }

}
