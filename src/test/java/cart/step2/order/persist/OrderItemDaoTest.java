package cart.step2.order.persist;

import cart.step2.order.domain.OrderEntity;
import cart.step2.order.domain.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

//    @DisplayName("List<OrderItem>을 입력받고 한 번에 저장한다.")
//    @Test
//    void batchInsert() {
//        // given
//        final OrderEntity orderEntity = OrderEntity.createNonePkOrder(1000, 1L, 1L);
//        Long orderId = orderDao.insert(orderEntity);
//
//        List<OrderItem> orderItems = new ArrayList<>();
//        orderItems.add(OrderItem.createNonePkOrder(1L, orderId, 2));
//        orderItems.add(OrderItem.createNonePkOrder(2L, orderId, 3));
//
//        // when
//        orderItemDao.batchInsert(orderItems);
//        List<OrderItem> responses = orderItemDao.findAll(1L);
//
//        // then
//        assertAll(
//                () -> assertThat(responses).extracting(OrderItem::getProductId)
//                        .contains(1L, 2L),
//                () -> assertThat(responses).extracting(OrderItem::getOrderId)
//                        .contains(1L, 1L),
//                () -> assertThat(responses).extracting(OrderItem::getQuantity)
//                        .contains(2, 3)
//        );
//    }

}
