package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

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

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderItemDao orderItemDao;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderItemDao = new OrderItemDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void 주문_식별자를_가진_주문상품_목록을_찾을_수_있다() {
        // given
        final Long orderId = orderDao.createOrder(new OrderEntity(1L, 8000)).getId();
        orderItemDao.saveAll(List.of(
                new OrderItemEntity(orderId, 1L, 3),
                new OrderItemEntity(orderId, 2L, 3)
        ));

        // when
        final List<OrderItemEntity> orderEntities = orderItemDao.findByOrderId(orderId);

        // then
        assertThat(orderEntities).hasSize(2);
    }
}
