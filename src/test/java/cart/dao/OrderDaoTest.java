package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.OrderEntity;
import java.sql.Timestamp;
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
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void 주문_엔티티를_받아_저장한다() {
        // given
        final OrderEntity orderEntity = new OrderEntity(null, 1L, null);

        // when
        orderDao.save(orderEntity);

        // then
        final List<OrderEntity> order = orderDao.findOrderByMemberId(1L);
        assertAll(
                () -> assertThat(order).hasSize(1),
                () -> assertThat(order.get(0).getId()).isPositive(),
                () -> assertThat(order.get(0).getMemberId()).isEqualTo(1L),
                () -> assertThat(order.get(0).getOrderTime()).isBefore(new Timestamp(System.currentTimeMillis()))
        );
    }

    @Test
    void 주문_엔티티_ID를_받아서_단건의_주문을_조회한다() {
        // given
        final OrderEntity orderEntity = new OrderEntity(null, 1L, null);

        // when
        final Long id = orderDao.save(orderEntity);

        // then
        final OrderEntity order = orderDao.findById(id).get();
        assertAll(
                () -> assertThat(order.getId()).isPositive(),
                () -> assertThat(order.getMemberId()).isEqualTo(1L),
                () -> assertThat(order.getOrderTime()).isBefore(new Timestamp(System.currentTimeMillis()))
        );
    }
}
