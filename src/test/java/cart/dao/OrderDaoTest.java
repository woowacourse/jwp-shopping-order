package cart.dao;

import cart.dao.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class OrderDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;

    @BeforeEach
    public void setup() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    public void 저장한다() {
        OrderEntity orderEntity = new OrderEntity(null, 1L, 1L, 0, 802000, null);

        Long orderId = orderDao.save(orderEntity);

        Optional<OrderEntity> createdOrder = orderDao.findById(orderId);
        assertThat(createdOrder).isPresent();
    }

    @Test
    public void 멤버_아이디로_조회한다() {
        long memberId = 1L;

        List<OrderEntity> orders = orderDao.findByMemberId(memberId);

        assertThat(orders).hasSize(1);
    }

    @Test
    public void 아이디로_조회한다() {
        long id = 1L;

        Optional<OrderEntity> order = orderDao.findById(id);

        assertThat(order.get().getId()).isEqualTo(id);
    }

    @Test
    public void 아이디로_조회_시_없으면_빈_값을_반환한다() {
        long id = 100L;

        Optional<OrderEntity> order = orderDao.findById(id);

        assertThat(order).isEmpty();
    }

    @Test
    public void 아이디로_삭제한다() {
        OrderEntity orderEntity = new OrderEntity(null, 1L, 1L, 0, 802000, null);
        long id = orderDao.save(orderEntity);

        orderDao.deleteById(id);

        Optional<OrderEntity> orders = orderDao.findById(id);
        assertThat(orders).isEmpty();
    }
}
