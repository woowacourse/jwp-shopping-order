package cart.step2.order.persist;

import cart.step2.order.domain.OrderEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @DisplayName("OrderEntity를 입력받아 orders를 저장한다.")
    @Test
    void insert() {
        // given
        final OrderEntity orderEntity = OrderEntity.createNonePkOrder(1000, 1L, 1L);

        // when
        orderDao.insert(orderEntity);
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(1L);

        // then
        assertThat(orderEntities).hasSize(1);
    }

    @DisplayName("입력받은 MemberId와 일치하는 Order들을 모두 조회한다.")
    @Test
    void findAllByMemberId() {
        // given
        final OrderEntity orderEntity1 = OrderEntity.createNonePkOrder(1000, 1L, 1L);
        final OrderEntity orderEntity2 = OrderEntity.createNonePkOrder(2000, 2L, 1L);
        orderDao.insert(orderEntity1);
        orderDao.insert(orderEntity2);

        // when
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(1L);

        // then
        Assertions.assertAll(
                () -> assertThat(orderEntities).hasSize(2),
                () -> assertThat(orderEntities).extracting(OrderEntity::getMemberId)
                        .contains(1L, 1L),
                () -> assertThat(orderEntities).extracting(OrderEntity::getPrice)
                        .contains(1000, 2000),
                () -> assertThat(orderEntities).hasSize(2)
        );
    }

}
