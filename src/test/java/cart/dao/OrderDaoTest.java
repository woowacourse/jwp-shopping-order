package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

import cart.entity.OrderEntity;
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
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void 주문을_저장할_수_있다() {
        // when
        final OrderEntity orderEntity = orderDao.createOrder(new OrderEntity(1L, 8000));

        // then
        assertAll(
                () -> assertThat(orderEntity.getId()).isNotNull(),
                () -> assertThat(orderEntity.getPrice()).isEqualTo(8000)
        );
    }

    @Test
    void 사용자_식별자를_가진_주문을_찾을_수_있다() {
        // given
        final Long memberId = 1L;
        final OrderEntity firstOrderEntity = orderDao.createOrder(new OrderEntity(memberId, 8000));
        final OrderEntity secondOrderEntity = orderDao.createOrder(new OrderEntity(memberId, 2000));
        final OrderEntity thirdOrderEntity = orderDao.createOrder(new OrderEntity(2L, 2000));

        // when
        final List<OrderEntity> orderEntities = orderDao.findAllByMemberId(memberId);

        // then
        assertAll(
                () -> assertThat(orderEntities).hasSize(2),
                () -> assertThat(orderEntities).extracting(
                        OrderEntity::getId, OrderEntity::getPrice
                ).contains(
                        tuple(firstOrderEntity.getId(), 8000),
                        tuple(secondOrderEntity.getId(), 2000)
                )
        );
    }
}
