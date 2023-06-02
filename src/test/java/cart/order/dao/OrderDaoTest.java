package cart.order.dao;

import cart.init.DBInit;
import cart.order.repository.OrderEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class OrderDaoTest extends DBInit {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void 주문을_저장한다() {
        // given
        final OrderEntity orderEntity = new OrderEntity(null, 1L, 5000L, 3000L, 500L);

        // when
        final Long orderId = orderDao.insert(orderEntity);

        // then
        assertThat(orderId).isOne();
    }

    @Test
    void memberId로_주문상세를_가져온다() {
        // given
        final OrderEntity orderEntity = new OrderEntity(null, 1L, 5000L, 3000L, 500L);
        final Long orderId = orderDao.insert(orderEntity);

        // when
        final List<OrderEntity> orderEntities = orderDao.findByMemberId(orderId);

        // then
        assertThat(orderEntities).hasSize(1);
    }
}
