package cart.dao;

import cart.entity.OrderItemEntity;
import cart.fixture.Fixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderItemDao orderItemDao;
    private OrderDao orderDao;
    private Long orderId;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        orderItemDao = new OrderItemDao(jdbcTemplate);

        orderId = orderDao.create(1L, 3000);
    }

    @Test
    void create() {
        Long orderItemId = orderItemDao.create(orderId, Fixture.치킨, 10);
        List<OrderItemEntity> orderItemEntities = orderItemDao.findAllByOrderId(orderId);
        List<Long> orderItemIds = orderItemEntities.stream().map(OrderItemEntity::getId).collect(Collectors.toList());
        Assertions.assertThat(orderItemIds).contains(orderItemId);
    }
}
