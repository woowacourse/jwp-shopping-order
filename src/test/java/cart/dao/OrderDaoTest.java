package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.repository.dto.OrderEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void 주문이_정상적으로_저장된다() {
        LocalDateTime createdAt = LocalDateTime.now();
        OrderEntity orderEntity = new OrderEntity(null, 1L, createdAt, 1000);

        Long orderId = orderDao.save(orderEntity);

        assertThat(orderId).isNotNull();
    }

    @Test
    void 주문이_정상적으로_조횐된다() {
        LocalDateTime createdAt = LocalDateTime.now();
        OrderEntity orderEntity = new OrderEntity(null, 1L, createdAt, 1000);

        Long orderId = orderDao.save(orderEntity);

        OrderEntity findOrder = orderDao.findById(orderId).get();

        assertAll(
                () -> assertThat(findOrder.getId()).isEqualTo(orderId),
                () -> assertThat(findOrder.getCreatedAt()).isEqualTo(createdAt),
                () -> assertThat(findOrder.getSpendPoint()).isEqualTo(1000)
        );
    }
}
