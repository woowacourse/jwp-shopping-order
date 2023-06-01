package cart.step2.order.persist;

import cart.step2.order.domain.OrderEntity;
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
        List<OrderEntity> orderEntities = orderDao.findAll(1L);

        // then
        assertThat(orderEntities).hasSize(1);
    }
    
}
