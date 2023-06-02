package cart.persistence.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderJdbcRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderJdbcRepository orderJdbcRepository;

    @BeforeEach
    void setUp() {
        this.orderJdbcRepository = new OrderJdbcRepository(jdbcTemplate);
    }

    @DisplayName("order를 저장한다.")
    @Test
    void createOrderTest() {
//        Order order = new Order(11400, 15000, 2000, MemberFixture.디노_ID포함, orderItems);
//        Long orderId = orderJdbcRepository.createOrder(order);
//        assertThat(orderId).isPositive();
    }
}
