package cart.dao;

import static cart.fixture.TestFixture.MEMBER_A;
import static cart.fixture.TestFixture.ORDER_ITEMS_ONE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.dao.dto.OrderDto;
import cart.domain.Order;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
    void 주문을_저장한다() {
        Order order = new Order(MEMBER_A, ORDER_ITEMS_ONE);
        Long id = orderDao.insert(OrderDto.of(order));

        assertThat(orderDao.selectBy(id)).isNotNull();
    }

    @Test
    void 주문을_불러온다() {
        Order order = new Order(MEMBER_A, ORDER_ITEMS_ONE);
        Long id = orderDao.insert(OrderDto.of(order));

        assertThat(orderDao.selectBy(id))
                .usingRecursiveComparison()
                .isEqualTo(new OrderDto(id, MEMBER_A.getId(), order.getCreatedAt()));
    }
}
