package cart.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.domain.OrderHistory;
import cart.domain.OrderProduct;
import cart.support.MemberTestSupport;
import cart.support.OrderHistoryTestSupport;
import cart.support.OrderProductTestSupport;
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
                OrderProductTestSupport.class, OrderProductDao.class, MemberDao.class
        })
class OrderProductDaoTest {

    private OrderProductDao orderProductDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OrderHistoryTestSupport orderHistoryTestSupport;
    @Autowired
    private OrderProductTestSupport orderProductTestSupport;

    @BeforeEach
    void init() {
        orderProductDao = new OrderProductDao(jdbcTemplate);
    }

    @DisplayName("실제 주문한 상품들을 추가한다.")
    @Test
    void addOrderProducts() {
        //given
        OrderHistory orderHistory = orderHistoryTestSupport.builder().build();
        OrderProduct orderProduct1 = orderProductTestSupport.builder().orderHistory(orderHistory).build();
        OrderProduct orderProduct2 = orderProductTestSupport.builder().orderHistory(orderHistory).build();
        List<OrderProduct> orderProducts = List.of(orderProduct1, orderProduct2);

        //when
        assertDoesNotThrow(() -> orderProductDao.insertAll(orderProducts));
    }
}