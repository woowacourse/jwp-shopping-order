package cart.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.domain.OrderHistory;
import cart.domain.OrderItem;
import cart.support.MemberTestSupport;
import cart.support.OrderHistoryTestSupport;
import cart.support.OrderItemTestSupport;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@Import(
        {OrderHistoryTestSupport.class, OrderHistoryDao.class, MemberTestSupport.class,
                OrderItemTestSupport.class, OrderItemDao.class, MemberDao.class
        })
class OrderItemDaoTest {

    private OrderItemDao orderItemDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OrderHistoryTestSupport orderHistoryTestSupport;
    @Autowired
    private OrderItemTestSupport orderItemTestSupport;

    @BeforeEach
    void init() {
        orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    @DisplayName("실제 주문한 상품들을 추가한다.")
    @Test
    void addOrderItems() {
        //given
        OrderHistory orderHistory = orderHistoryTestSupport.builder().build();
        OrderItem orderItem1 = orderItemTestSupport.builder().orderHistory(orderHistory).build();
        OrderItem orderItem2 = orderItemTestSupport.builder().orderHistory(orderHistory).build();
        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);

        //when
        assertDoesNotThrow(() -> orderItemDao.insertAll(orderItems));
    }
}