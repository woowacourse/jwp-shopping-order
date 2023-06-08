package cart.domain.order.persistence;

import static cart.fixtures.OrderFixtures.Dooly_Order1;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.order.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        this.orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    @DisplayName("받은 주문을 삽입한다.")
    void insert() {
        // given
        Order order = Dooly_Order1.DOMAIN();

        // when
        Order insertedOrder = orderDao.insert(order);

        // then
        assertThat(insertedOrder).isNotNull();
    }

    @Test
    @DisplayName("주문 ID에 해당하는 행이 없으면 TRUE를 반환한다.")
    void isNotExistById_true() {
        // given
        Long notExistId = -1L;

        // when, then
        assertThat(orderDao.isNotExistById(notExistId)).isTrue();
    }

    @Test
    @DisplayName("주문 ID에 해당하는 행이 없으면 FALSE를 반환한다.")
    void isNotExistById_false() {
        // given
        Long orderId = Dooly_Order1.ID;

        // when, then
        assertThat(orderDao.isNotExistById(orderId)).isFalse();
    }
}
