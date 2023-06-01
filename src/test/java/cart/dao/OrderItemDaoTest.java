package cart.dao;

import static cart.fixture.OrderFixture.ORDER_1;
import static cart.fixture.OrderItemsFixture.ORDER_ITEMS_1;
import static cart.fixture.ProductFixture.PRODUCT_1;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderItemDao orderItemDao;
    private OrderDao orderDao;
    private ProductDao productDao;
    private Long orderId;
    private Long productId;

    @BeforeEach
    void setUp() {
        orderItemDao = new OrderItemDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);

        orderId = orderDao.save(ORDER_1);
        productId = productDao.createProduct(PRODUCT_1);
    }

    @Test
    @DisplayName("주문 상품들을 저장한다.")
    void save() {
        assertDoesNotThrow(
                () -> orderItemDao.saveAll(ORDER_ITEMS_1, orderId)
        );
    }
}
