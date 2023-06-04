package cart.dao;

import static cart.fixture.OrderFixture.ORDER_1;
import static cart.fixture.OrderItemsFixture.ORDER_ITEMS_1;
import static cart.fixture.ProductFixture.PRODUCT_1;
import static cart.fixture.ProductFixture.PRODUCT_2;
import static cart.fixture.ProductFixture.PRODUCT_3;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.entity.OrderItemEntity;
import java.util.List;
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

    @BeforeEach
    void setUp() {
        orderItemDao = new OrderItemDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);

        orderId = orderDao.save(ORDER_1);
        productDao.createProduct(PRODUCT_1);
        productDao.createProduct(PRODUCT_2);
        productDao.createProduct(PRODUCT_3);
    }

    @Test
    @DisplayName("주문 상품들을 저장한다.")
    void saveAll() {
        assertDoesNotThrow(
                () -> orderItemDao.saveAll(ORDER_ITEMS_1, orderId)
        );
    }

    @Test
    @DisplayName("주문 ID로 주문 상품들을 조회한다.")
    void findAllByOrderId() {
        // given
        orderItemDao.saveAll(ORDER_ITEMS_1, orderId);

        // when
        List<OrderItemEntity> orderItemEntities = orderItemDao.findAllByOrderId(orderId);

        // then
        assertThat(orderItemEntities)
                .hasSize(3);
    }
}
