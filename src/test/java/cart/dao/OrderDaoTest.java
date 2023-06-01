package cart.dao;

import cart.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static cart.ShoppingOrderFixture.member1;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ActiveProfiles("test")
@JdbcTest
class OrderDaoTest {

    private static final Order CHICKEN_ORDER = new Order(member1,10000L, 5000L, 1000L);
    private static final Order PIZZA_ORDER = new Order(member1, 13000L, 0L, 1300L);

    private OrderDao orderDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @DisplayName("findById 테스트")
    @Test
    void findByIdTest() {
        Long orderId = orderDao.save(CHICKEN_ORDER);
        Order order = orderDao.findById(orderId);

        assertSoftly(softly -> {
            softly.assertThat(order.getId()).isEqualTo(orderId);
            softly.assertThat(order.getMember()).isEqualTo(CHICKEN_ORDER.getMember());
            softly.assertThat(order.getOriginalPrice()).isEqualTo(CHICKEN_ORDER.getOriginalPrice());
            softly.assertThat(order.getPointToAdd()).isEqualTo(CHICKEN_ORDER.getPointToAdd());
        });
    }

    @DisplayName("findByMemberId 테스트")
    @Test
    void findByMemberIdTest() {
        orderDao.save(CHICKEN_ORDER);
        orderDao.save(PIZZA_ORDER);
        List<Order> orders = orderDao.findByMemberId(CHICKEN_ORDER.getMember().getId());

        assertSoftly(softly -> {
            softly.assertThat(orders).hasSize(2);
            softly.assertThat(orders.get(0).getOriginalPrice()).isEqualTo(CHICKEN_ORDER.getOriginalPrice());
            softly.assertThat(orders.get(0).getUsedPoint()).isEqualTo(CHICKEN_ORDER.getUsedPoint());
            softly.assertThat(orders.get(0).getPointToAdd()).isEqualTo(CHICKEN_ORDER.getPointToAdd());
            softly.assertThat(orders.get(1).getOriginalPrice()).isEqualTo(PIZZA_ORDER.getOriginalPrice());
            softly.assertThat(orders.get(1).getUsedPoint()).isEqualTo(PIZZA_ORDER.getUsedPoint());
            softly.assertThat(orders.get(1).getPointToAdd()).isEqualTo(PIZZA_ORDER.getPointToAdd());
        });
    }
}
