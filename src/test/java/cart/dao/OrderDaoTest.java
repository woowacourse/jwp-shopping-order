package cart.dao;

import cart.entity.OrderEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void create() {
        Long orderId = orderDao.create(1L, 3000);
        Optional<OrderEntity> findOrderEntity = orderDao.findById(orderId);

        Assertions.assertAll(
                () -> assertThat(findOrderEntity).isNotEqualTo(Optional.empty()),
                () -> assertThat(findOrderEntity.get().getId()).isEqualTo(orderId)
        );
    }

    @Test
    void findAllByMemberId() {
        Long orderId1 = orderDao.create(1L, 3000);
        Long orderId2 = orderDao.create(1L, 3000);

        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(1L);
        List<Long> orderIds = orderEntities.stream().map(OrderEntity::getId).collect(Collectors.toList());

        assertThat(orderIds).contains(orderId1, orderId2);
    }
}
