package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderDao orderDao;
    private OrderItemDao orderItemDao;

    @BeforeEach
    void setUp() {
        orderItemDao = new OrderItemDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void 주문_목록_엔티티를_받아_저장한다() {
        // given
        final OrderEntity orderEntity = new OrderEntity(null, 1L, null);
        final Long orderId = orderDao.save(orderEntity);
        final OrderItemEntity orderItem1 = new OrderItemEntity(null, orderId, 1L, 2000, 5);
        final OrderItemEntity orderItem2 = new OrderItemEntity(null, orderId, 2L, 3000, 4);

        // expect
        assertDoesNotThrow(() -> orderItemDao.batchInsert(List.of(orderItem1, orderItem2)));
    }

    @Test
    void 주문_ID를_받아서_해당_하는_주문의_모든_주문_목록을_조회한다() {
        // given
        final OrderEntity orderEntity = new OrderEntity(null, 1L, null);
        final Long orderId = orderDao.save(orderEntity);
        final OrderItemEntity orderItem1 = new OrderItemEntity(null, orderId, 1L, 2000, 5);
        final OrderItemEntity orderItem2 = new OrderItemEntity(null, orderId, 2L, 3000, 4);

        // when
        orderItemDao.batchInsert(List.of(orderItem1, orderItem2));
        final List<OrderItemEntity> orderItems = orderItemDao.findByOrderId(orderId);

        // then
        assertThat(orderItems).hasSize(2);
        assertThat(orderItems).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(orderItem1, orderItem2));
    }

}
