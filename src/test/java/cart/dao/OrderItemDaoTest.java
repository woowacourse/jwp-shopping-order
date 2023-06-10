package cart.dao;

import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class OrderItemDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderItemDao orderItemDao;

    @BeforeEach
    public void setUp() {

        orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    @Test
    public void 여러_내역을_저장한다() {
        Long orderId = new OrderDao(jdbcTemplate).save(new OrderEntity(null, 1L, null, 0, 80000, null));
        List<OrderItemEntity> orderItems = List.of(
                new OrderItemEntity(null, orderId, 1L, "지구", 1000, "https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg", 2),
                new OrderItemEntity(null, orderId, 2L, "화성", 200000, "https://cdn.pixabay.com/photo/2011/12/13/14/30/mars-11012__480.jpg", 4)
        );

        orderItemDao.saveAll(orderItems);

        List<OrderItemEntity> savedOrderItems = orderItemDao.finByOrderId(orderId);
        assertThat(savedOrderItems).hasSize(2);
    }

    @Test
    public void 아이디_목록으로_조회한다() {
        List<Long> orderIds = List.of(1L, 2L);

        List<OrderItemEntity> orderItems = orderItemDao.findByOrderIds(orderIds);

        assertAll(
            () -> assertThat(orderItems.get(0).getName()).isEqualTo("지구"),
            () -> assertThat(orderItems.get(1).getName()).isEqualTo("화성")
        );
    }

    @Test
    public void 주문_아이디로_조회한다() {
        Long orderId = 1L;

        List<OrderItemEntity> orderItems = orderItemDao.finByOrderId(orderId);

        assertThat(orderItems).hasSize(2);
    }

    @Test
    public void 주문_아이디로_삭제한다() {
        Long orderId = 1L;

        orderItemDao.deleteByOrderId(orderId);

        List<OrderItemEntity> orderItems = orderItemDao.finByOrderId(orderId);
        assertThat(orderItems).hasSize(0);
    }

}
