package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.OrderItem;
import cart.domain.Product;
import cart.repository.dto.OrderEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderItemDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    OrderItemDao orderItemDao;
    OrderDao orderDao;
    ProductDao productDao;
    Long orderId;
    Long productId;

    @BeforeEach
    void setUp() {
        orderItemDao = new OrderItemDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        orderId = orderDao.save(new OrderEntity(null, 1L, LocalDateTime.now(), 1000));
        productId = productDao.save(new Product(null, "벨리곰", 1000, "http:test"));
    }

    @Test
    void 주문_상품이_정상적으로_저장된다() {
        Product product = productDao.findById(productId).get();
        OrderItem orderItem = new OrderItem(null, product, 10, 10000);

        Long orderItemId = orderItemDao.save(orderId, orderItem);

        assertThat(orderItemId).isNotNull();
    }

    @Test
    void 주문_상품이_정상적으로_조회된다() {
        Product product = productDao.findById(productId).get();
        OrderItem orderItem = new OrderItem(null, product, 10, 10000);

        orderItemDao.save(orderId, orderItem);
        List<OrderItem> orderItems = orderItemDao.findByOrderId(orderId);

        assertAll(
                () -> assertThat(orderItems).hasSize(1),
                () -> assertThat(orderItems.get(0).getProduct()).isEqualTo(orderItem.getProduct()),
                () -> assertThat(orderItems.get(0).getPrice()).isEqualTo(orderItem.getPrice()),
                () -> assertThat(orderItems.get(0).getQuantity()).isEqualTo(orderItem.getQuantity())
        );
    }
}
