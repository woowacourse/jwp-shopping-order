package cart.dao;

import static cart.fixture.TestFixture.ORDER_ITEMS_ONE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.OrderItem;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
class OrderItemDaoTest {

    private static final Long ORDER_ID = 1L;
    @Autowired
    JdbcTemplate jdbcTemplate;
    private OrderItemDao orderItemDao;

    @BeforeEach
    void setUp() {
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    @Test
    void 주문_항목을_모두_저장한다() {
        orderItemDao.insertAll(ORDER_ID, ORDER_ITEMS_ONE);
    }

    @Test
    void 주문_항목을_모두_조회한다() {
        orderItemDao.insertAll(ORDER_ID, ORDER_ITEMS_ONE);
        List<OrderItem> orderItems = orderItemDao.selectAllOf(ORDER_ID);

        assertThat(orderItems)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrderElementsOf(ORDER_ITEMS_ONE);
    }
}
